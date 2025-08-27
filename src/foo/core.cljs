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
    [:table
     [:tbody {:key k}
      (doall
        (for [i (range 100)]
          [:tr {:key i}
           (doall
             (for [j (range 100)]
               [:td {:key j}]))]))]]))

(defn make-obj []
  {:rows (for [i (range 20)]
           {:a (rand-nth [true false])
            :b (rand)
            :c (rand)
            :d (rand)})})

(def objs [(make-obj)
           (make-obj)])

(defn init []
  (dom/render [leak-view] (.getElementById js/document "root"))
  (go
    (doseq [obj (cycle objs)]
      (reset! obj* obj)
      (println "setting obj*")
      (<! (timeout 10)))))

