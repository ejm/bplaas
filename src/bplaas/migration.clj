(ns bplaas.migration
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as sql]))

(defn create-pickup-lines []
  (sql/with-connection (System/getenv "DATABASE_URL")
    (sql/create-table :pickuplines
      [:id :serial "PRIMARY KEY"]
      [:pickup_line :varchar "NOT NULL"]
      [:twitter_source :varchar "NOT NULL"])))

(defn get-data-from-line
  "Gets the actual pickup line and twitter source from the csv line"
  [v]
  {:pickup_line (nth v 2), :twitter_source (nth v 0)})

(defn read-csv-file
  [csv-filename]
  (with-open [in-file (io/reader csv-filename)]
    (doall
      (csv/read-csv in-file :separator \tab))))

(defn insert-data
  "inserts a single pickup line and source into the database"
  [pickup-line]
  (sql/with-connection (System/getenv "DATABASE_URL")
    (sql/insert-record :pickuplines pickup-line)))

(defn load-data
  [csv-filename]
  (doall (map #(insert-data (get-data-from-line %)) (read-csv-file csv-filename))))

(defn -main []
  (println "Creating database structure...")
  (println (str "database url " (System/getenv) "."))
  (create-pickup-lines)
  (load-data "pu_lines.csv")
  (println " done"))
