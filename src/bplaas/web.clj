(ns bplaas.web
  (:use compojure.core
        ring.middleware.json
        ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.data.json :refer (read-json json-str)]
            [ring.util.response :as resp]
            [ring.adapter.jetty :as jetty]
            [clojure.java.jdbc :as sql]
)
  (:gen-class))

(defn get-random-pickup-line
  []
  (sql/with-connection (System/getenv "HEROKU_POSTGRESQL_SILVER_URL")
    (sql/with-query-results results
      ["select pickup_line, twitter_source from pickuplines offset random() * (select count(*) from pickuplines) limit 1;"]
      (into [] results))))

(defroutes app-routes

  (GET "/pickup-line" []
    (json-str (first (get-random-pickup-line))))
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
