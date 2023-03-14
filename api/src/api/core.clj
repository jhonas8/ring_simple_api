(ns api.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json])
  (:gen-class))

(defn start []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (server/run-server (fn [req] (route/not-found))
                       {:port port
                        :join? false
                        :handler (wrap-defaults site-defaults
                                                (route/not-found "Error! Page not found"))})))

(defn -main
  [& args]
  (start)
  (println "Server started on port 3000"))
