(ns laoban.core
  (:require [reagent.core :as r] ))

(enable-console-print!)


(defonce app-state (r/atom {:columns [{:title "Todos"
                                       :cards [{:title "Write a Reagent app"}]}]}))

(defn Card [card]
  [:div {:className "card"} (:title card)])

(defn NewCard []
  [:div {:className "new-card"} "+ add new card"])

(defn Column [col]
  [:div {:className "column"}
   [:h2 (:title col)]
   (for [c (:cards col)]
     [Card c])
   [NewCard]])

(defn NewColumn []
  [:div {:className "new-column"} "+ add new column"])

(defn Board []
  [:div {:className "board"}
   (for [c (:columns @app-state)]
     [Column c])
   [NewColumn]])

(r/render [Board] (js/document.getElementById "app"))
