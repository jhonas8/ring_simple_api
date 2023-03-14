(ns api.routes
  (:require [compojure.core :refer :all]
           [compojure.route :as route]
           [ring.middleware.defaults :refer :all]
           [clojure.pprint :as pp]
           [clojure.string :as str]
           [clojure.data.json :as json])
  (:gen-class))

(declare defroutes)
(declare GET)

(defroutes []
  (GET "/" [] "Hello World")
  (GET "/:name" [name] (str "Hello, " name))
  (route/not-found "Error! Page not found"))
