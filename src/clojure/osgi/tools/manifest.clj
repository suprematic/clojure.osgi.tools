(ns clojure.osgi.tools.manifest
  "Tools for working with OSGi bundle manifests"
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.pprint :as pp]
    [clojure.string :as str]
    [cheshire.core :as chsh])
  (:import
    [java.util.jar Manifest]
    [java.util.jar Attributes$Name]
    [java.util Date]
    ))

(def HEADER_BUNDLE_SYMBOLIC_NAME "Bundle-SymbolicName")
(def HEADER_BUNDLE_VERSION "Bundle-Version")
(def HEADER_EXPORT_PACKAGE "Export-Package")

(defn- manifest [uri]
  (Manifest. (io/input-stream uri)))

(defn- jar-file-url [jarpath filepath]
  (io/as-url (str "jar:file:" jarpath "!" filepath)))

(defn- manifest-url [jarpath]
  (jar-file-url jarpath "/META-INF/MANIFEST.MF"))


(defn- header-value [attrs header-name]
  (if-let [raw-value (.get attrs (Attributes$Name. header-name))]
    (str/replace raw-value #" " "")))

(defn- find-bundle-files [dirpath]
  (filter #(re-matches #".+\.jar" (.getName %))
          (file-seq (io/file dirpath))))

(defn- assoc-header
  ([m attrs header-key]
   (assoc-header m attrs header-key identity))
  ([m attrs header-key transformer]
   (if-let [v (header-value attrs header-key)]
     (assoc m header-key (transformer v))
     m)))

(defn- split-packages [s]
  (-> s
      (str/replace #";x-friends:=\".+\"" "")
      (str/replace #";uses:=\".+\"" "")
      (str/split #",")))

(defn- analyze [attrs]
  (-> {}
      (assoc-header attrs HEADER_BUNDLE_SYMBOLIC_NAME)
      (assoc-header attrs HEADER_BUNDLE_VERSION)
      (assoc-header attrs HEADER_EXPORT_PACKAGE split-packages)))


(defn- write-edn! [struct file]
  (spit file (with-out-str (pp/pprint struct))))

(defn- write-json! [struct file]
  (spit file (chsh/generate-string struct {:pretty true})))

(defn- write-plaintext-exports-only! [struct file]
  (let [
        bundle-to-packages (reduce
                             #(assoc %1 %2 (get (get struct %2) HEADER_EXPORT_PACKAGE) )
                             {}
                             (keys struct))
        for-printing (reduce
                       #(assoc
                         %1
                         (key %2)
                         (apply str (interpose "\n" (sort (val %2)))))
                       {}
                       bundle-to-packages)
        out-string (apply str (interpose "\n" (interleave (keys for-printing) (vals for-printing))))
        ]
    (spit file (with-out-str (print out-string)))
    (print out-string)))

(defn- filename-out [timestamp type]
  (str "report_" timestamp "." type))

(defn report
  "Analyzes bundles within the given input directory
  and creates simple reports (in EDN, JSON and plaintext)
  in the given output directory."
  [dir-in dir-out]

  (let [files (find-bundle-files dir-in)
        manifest-urls (map #(manifest-url (.getAbsolutePath %)) files)
        manifests (map manifest manifest-urls)
        attributes (map #(.getMainAttributes %) manifests)
        analyzed (map analyze attributes)
        filenames (map #(.getAbsolutePath %) files)
        result (zipmap filenames analyzed)
        ts (.getTime (Date.))
        file-out (fn[type] (io/file dir-out (filename-out ts type)))]
    (write-edn! result (file-out "edn"))
    (write-json! result (file-out "json"))
    (write-plaintext-exports-only! result (file-out "txt"))
    result))

(defn -main [& args]
  (println "Started with args:" args)
  (report (first args) (second args)))