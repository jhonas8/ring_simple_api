(ns api.core
  (:gen-class)
  (:require
    [com.mitranim.forge :as forge]
    [com.stuartsierra.component :as component]
    [api.util :as util :refer [getenv]]
    [api.server]
    [api.datomic]))

(defn create-system [prev-sys]
  (component/system-map
    :dat (api.dat/new-dat prev-sys (getenv "DB_URI"))
    :server (api.server/new-server prev-sys)))

(defn -main []
  (println "Starting system on thread" (str (Thread/currentThread)) "...")
  (forge/reset-system! create-system))

(defn -main-dev []
  (forge/start-development! {:system-symbol `create-system})
  (forge/reset-system! create-system)
  (println "Started server on" (str "http://localhost:" (getenv "LOCAL_PORT"))))
