(ns foo.bar.bnd
	(require 
    [clojure.tools.logging :as log]
    [clojure.osgi.tools.bnd :as tools]))

(def local-sources
  [{
    :id "eclipse-platform.3.8.2"
    :dir "c:/eclipse-distros/R-3.8.2-201301310800/features/"
  }
  {
    :id "simultaneous-indigo.3.8.2"
    :dir "c:/eclipse-distros/indigo/201202240900/features/"
  }
  {
    :id "eclipse-platform.4.4.0"
    :dir "c:/eclipse-distros/R-4.4-201406061215/features/"
  }
  {
    :id "simultaneous-luna.4.4.0"
    :dir "c:/eclipse-distros/luna/201409261001/features/"
  }])

(def src {
    :id "eclipse-platform.3.8.2"
    :dir "c:/eclipse-distros/R-3.8.2-201301310800/features/" })

(defn create-bnd-libs
  "Creates BND libs into the given directory."
  ([output-root-dir]
    (log/info "Processing the default sources")
    (doall (map #(create-bnd-libs output-root-dir %) local-sources)))
  ([output-root-dir {:keys [dir id]}]
    (let [dir-out (str output-root-dir "/" id)]
          (log/info "Processing" id "into" dir-out)
          (tools/create-libs-from-pde-features dir dir-out))))