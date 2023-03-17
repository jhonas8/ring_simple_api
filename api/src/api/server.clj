(ns api.server
  (:require
    [com.mitranim.forge :as forge :refer [sys]]
    [com.stuartsierra.component :as component]
    [org.httpkit.server :refer [run-server]]
    [hiccup.page :refer [html5]]
    [compojure.core :as compojure :refer [GET POST]]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
    [clojure.walk :refer [keywordize-keys]]
    [ring.util.anti-forgery :refer [anti-forgery-field]]
    [api.util :as util :refer [getenv]]
    [api.datomic :as dat])
  (:import
    [org.httpkit.server HttpServer]))


(defn html-head [& content]
  [:head
   [:base {:href "/"}]
   [:meta {:charset "utf-8"}]
   [:link {:rel "icon" :href "data:;base64,="}]
   content])


(defn pp-str [value] (with-out-str (clojure.pprint/pprint value)))


(defn index-page [req]
  (let [dat (:dat sys)
        db @dat
        comments (when db (dat/q-comments db))]
    (html5
      (html-head
        [:title "Simple Ring API"])
      [:body
       [:h3 "Datomic Starter"]
       [:p "Database connection: "
        [:pre  (:conn dat)]]
       (when (:conn dat)
        [:p "Database URI: "
         [:pre (:uri dat)]])])))


(defn submit-comment [req]
  (let [body   (util/sanitize-permit-html (-> req :params :comment/body))
        result (dat/create-comment (:dat sys) body)]
    {:status 303
     :headers {"Location" "/"}
     :flash result}))



(def handler
  (->
    (compojure/routes
      (GET "/" [] index-page)
      (POST "/comment" [] submit-comment))
    util/wrap-keywordize-params
    (wrap-defaults site-defaults)
    forge/wrap-development-features))



(defrecord Server [^HttpServer http-server]
  component/Lifecycle
  (start [this]
    (when http-server
      (.stop http-server 100))
    (let [port (Long/parseLong (getenv "LOCAL_PORT"))]
      (assoc this :http-server
        (-> (run-server handler {:port port}) meta :server))))
  (stop [this]
    (when http-server
      (.stop http-server 100))
    (assoc this :http-server nil)))

(defn new-server [prev-sys]
  (new Server nil))
