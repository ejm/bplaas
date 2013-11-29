(ns bplaas.migration
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as sql]))

(defn create-pickup-lines []
  ; drop the table to ensure it doesn't exist
  (sql/with-connection (System/getenv "HEROKU_POSTGRESQL_SILVER_URL")
    (try (sql/drop-table :pickuplines) (catch Exception e (str "Database table didn't exist")))
    (sql/create-table :pickuplines
      [:id :serial "PRIMARY KEY"]
      [:pickup_line :varchar "NOT NULL"])))

(defn get-data-from-line
  "Gets the actual pickup line and twitter source from the csv line"
  [v]
  {:pickup_line (nth v 0)})

(defn read-csv-file
  [csv-filename]
  (with-open [in-file (io/reader csv-filename)]
    (doall
      (csv/read-csv in-file))))

(defn insert-data
  "inserts a single pickup line and source into the database"
  [pickup-line]
  (sql/with-connection (System/getenv "HEROKU_POSTGRESQL_SILVER_URL")
    (sql/insert-record :pickuplines pickup-line)))

(defn load-data
  [csv-filename]
  ; the first line of the csv file has the headers
  (doall (map #(insert-data (get-data-from-line %)) (rest (read-csv-file csv-filename)))))

(defn -main []
  (println "Creating database structure...")
  (println (str "database url " (System/getenv "HEROKU_POSTGRESQL_SILVER_URL") "."))
  (create-pickup-lines)
  (load-data "pu_lines.csv")
  (println " done"))
