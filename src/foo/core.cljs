(ns foo.core
  (:require-macros
    [cljs.core.async.macros :refer [go]])
  (:require
    [cljs.core.async :refer [timeout <!]]
    [reagent.core :as r]
    [reagent.dom :as dom]))

(defonce obj* (r/atom nil))

(defn leak-view []
  (let [k (hash @obj*)]
    [:table {:key k}  ;; <-- {:key k} here is the simplest way to cause the memory leak, but can be moved to :tr as well
     (for [i (range 100)]
       [:tr {:key i}
        (for [j (range 100)]
          [:td {:key j}])])]))

(defn make-obj []
  {:nums (repeatedly 20 rand)})

(def objs [(make-obj)
           (make-obj)])

(defn init []
  (dom/render [leak-view] (.getElementById js/document "root"))
  (go
    (doseq [obj (cycle objs)]
      (reset! obj* obj)
      (println "setting obj*")
      (<! (timeout 10)))))

