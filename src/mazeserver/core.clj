(ns mazeserver.core
  [next.jdbc :as jdbc]
  (:require [next.jdbc :as jdbc]))

(def db {
         :dbtype "h2"
         :dbname "data/maze"
         :user "sa" :password ""})
