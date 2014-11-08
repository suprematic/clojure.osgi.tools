(defproject clojure.osgi.tools "0.1.1-SNAPSHOT"
            :description "A Clojure library providing utilities for working with miscellaneous OSGi tools (BND, PDE, etc.)."
            :url "https://github.com/suprematic/clojure.osgi.tools"
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [
                           [org.clojure/clojure "1.5.1"]
                           [org.clojure/tools.logging "0.3.1"]
                           [log4j/log4j "1.2.17"]
                           [cheshire "5.3.1"]]
            :source-paths ["src", "src.example"]

            :main clojure.osgi.tools.manifest
            :aot [clojure.osgi.tools.manifest]
            )
