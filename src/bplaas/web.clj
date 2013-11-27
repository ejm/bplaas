(ns bplaas.web
  (:use compojure.core
        ring.middleware.json
        ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.data.json :refer (read-json json-str)]
            [ring.util.response :as resp]
            [ring.adapter.jetty :as jetty]
)
  (:gen-class))

(defroutes app-routes

  (GET "/pickup-line" []
       (json-str {
                  :pickup_line "test"
                  :twitter_source "blah"
                  }))
  (GET "/" [] (resp/file-response "index.html" {:root "resources"}))
  (route/files "/" {:root "resources"})
  (route/not-found (json-str {:error "not found"}))
)

(defn init []
  )

(defn destroy []
  )

(def app
  (handler/site app-routes))


(defn -main
  ([] (-main 80))
  ([port]
      (jetty/run-jetty app {:port (Integer. port) :join? false})))
