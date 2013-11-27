(ns bplaas.core-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [bplaas.core :refer :all]
            [clojure.java.jdbc :as sql]))

(with-state-changes
  [
   (before :facts 
           (sql/with-connection (System/getenv "DATABASE_URL")
             (sql/create-table :testing [:data :text])))
   (before :facts 
           (sql/with-connection (System/getenv "DATABASE_URL")
             (sql/insert-record :testing {:data "Hello World"})))
   (after :facts 
          (sql/with-connection (System/getenv "DATABASE_URL")
            (sql/drop-table :testing)))
   ]
  (fact "Test a connection can be made to the database"
        (sql/with-connection (System/getenv "DATABASE_URL")
          (sql/with-query-results results
            ["select * from testing"]
            (into [] results))) => [{:data "Hello World"}]
            )
         )
