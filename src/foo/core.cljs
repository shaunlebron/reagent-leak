(ns foo.core
  (:require-macros
    [cljs.core.async.macros :refer [go]])
  (:require
    [cljs.core.async :refer [timeout <!]]
    [reagent.core :as r]
    [reagent.dom :as rd]
    [reagent.dom.client :as rdc]))

(defonce obj* (r/atom nil))

(defn leak-view []
  (let [k (hash @obj*)]
    [:table {:key k}  ;; <-- {:key k} here is the simplest way to cause the memory leak, but can be moved to :tr as well
     [:tbody
      (for [i (range 100)]
        [:tr {:key i}
         (for [j (range 100)]
           [:td {:key j}])])]]))

(defn make-obj []
  {:nums (repeatedly 20 rand)})

(def objs [(make-obj)
           (make-obj)])

(def REACT_VERSION 18) ;; <-- leak happens in both React 17 and 18

(defn init []
  (let [root (js/document.getElementById "root")]
    (case REACT_VERSION
      17 (rd/render [leak-view] root) ;; <-- falls back to React 17 behavior
      18 (rdc/render (rdc/create-root root) [leak-view])))

  (go
    (doseq [obj (cycle objs)]
      (reset! obj* obj)
      (println "setting obj*")
      (<! (timeout 10)))))

