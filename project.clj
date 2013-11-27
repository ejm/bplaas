(defproject bplaas "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [ring/ring-json "0.2.0"]
                 [ring "1.1.8"]
                 [org.clojure/data.json "0.2.2"]
                 [postgresql "9.1-901.jdbc4"]
                 [org.clojure/data.csv "0.1.2"]]
  :plugins [[lein-ring "0.8.5"]]
  :min-lein-version "2.0.0"
  :main bplaas.web
  :uberjar-name "bplaas.jar"
  :ring {:handler bplaas.web/app
         :init    bplaas.web/init
         :destroy bplaas.web/destroy}

  :profiles {
             :dev {:plugins [[lein-midje "3.0.0"]]
                   :dependencies [[midje "1.5.1"]]}}

)
