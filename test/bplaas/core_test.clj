(ns bplaas.core-test
  (:require [clojure.test :refer :all]
            [bplaas.core :refer :all]
            [clojure.java.jdbc :as sql]))

(deftest database-url-test
    ; create a table
    (sql/with-connection (System/getenv "DATABASE_URL")
      (sql/create-table :testing [:data :text]))
    ; insert some data and read it back
    (sql/with-connection (System/getenv "DATABASE_URL")
      (sql/insert-record :testing {:data "Hello World"}))

  (testing "Test a connection can be made to the database"
    (is (= [{:data "Hello World"}]
          (sql/with-connection (System/getenv "DATABASE_URL")
            (sql/with-query-results results
              ["select * from testing"]
              (into [] results))))))
    ; drop the table
    (sql/with-connection (System/getenv "DATABASE_URL")
      (sql/drop-table :testing)))
