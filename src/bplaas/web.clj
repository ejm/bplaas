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
  (GET "/" [] 
       "home")
  (route/not-found (json-str {:error "not found"})))

(defn init []
  )

(defn destroy []
  )

(def app
  (->
   (handler/site app-routes)
   (wrap-json-body)
   (wrap-json-params)
   (wrap-json-response)))


(defn -main
  ([] (-main 80))
  ([port]
      (jetty/run-jetty app {:port (Integer. port) :join? false})))
