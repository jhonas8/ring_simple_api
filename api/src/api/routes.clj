(ns api.routes
  (:require [compojure.core :refer :all]
           [compojure.route :as route]
           [ring.middleware.defaults :refer :all]
           [clojure.pprint :as pp]
           [clojure.string :as str]
           [clojure.data.json :as json])
  (:gen-class))

(defn simple-body-page [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "<html><body><h1>Hello World</h1></body></html>"})

(defn request-example [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (->>
           (pp/pprint req)
           (str "Request Object: " req))})

(defn params-example [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "<html><body><h1>Params Example</h1><p>Params: " (-> req :params :name) "</p></body></html>")})

(defroutes app-routes 
  (GET "/" [] simple-body-page)
  (GET "/request" [] request-example)
  (GET "/params" [] params-example)
  (route/not-found "Error! Page not found"))
