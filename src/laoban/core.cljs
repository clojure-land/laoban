(ns laoban.core
  (:require goog.events
            devtools.core
            [reagent.core :as r]
            ))

(devtools.core/install!)

(enable-console-print!)


(def app-state (r/atom {:columns [{:title "Todos"
                                   :cards [{:title "Write a Reagent app"}
                                           {:title "Get more customers"}]}]}))

(defn stop-editing [card]
  (dissoc card :editing))

(defn start-editing [card]
  (assoc card :editing true))

(defn stop-editing-col [col]
  (update col :cards (partial mapv stop-editing)))


(defn Card [card]
  (let [{:keys [editing title]} @card]
    (if editing
      [:div {:className "card editing"}
       [:input {:type "text"
                :value title
                :autoFocus true
                :on-change (fn [e] (swap! card assoc :title (.. e -target -value)))
                :on-blur #(swap! card stop-editing)}]]
      [:div {:className "card"
             :on-click (fn [e]
                         (swap! card start-editing))} title])))

(defn NewCard []
  [:div {:className "new-card"} "+ add new card"])

(defn Column [col]
  [:div {:className "column"}
   [:h2 (:title @col)]
   (for [i (range (count (:cards @col)))]
     [Card (r/cursor col [:cards i])])
   [NewCard]])

(defn NewColumn []
  [:div {:className "new-column"} "+ add new column"])

(defn Board []
  [:div {:className "board"}
   (for [i (range (count (:columns @app-state)))]
     [Column (r/cursor app-state [:columns i])])
   [NewColumn]])

(r/render [Board] (js/document.getElementById "app"))
