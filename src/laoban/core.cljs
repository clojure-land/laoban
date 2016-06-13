(ns laoban.core
  (:require goog.events
            devtools.core
            [reagent.core :as r]
            ))

(devtools.core/install!)

(enable-console-print!)


(defonce app-state (r/atom {:columns [{:title "Todos"
                                       :cards [{:title "Write a Reagent app"}
                                               {:title "Get more customers"}]}]}))


(defn stop-editing [c]
  (dissoc c :editing))

(defn start-editing [c]
  (assoc c :editing true))

(defn stop-editing-col [col]
  (update col :cards (partial mapv stop-editing)))

(defn Editable [props type cur]
  (let [{:keys [editing title]} @cur]
    (if editing
      [type (update props :className #(str % " editing"))
       [:input {:type "text"
                :value title
                :autoFocus true
                :on-change (fn [e] (swap! cur assoc :title (.. e -target -value)))
                :on-blur #(swap! cur stop-editing)}]]
      [type (assoc props
                   :on-click (fn [e]
                               (swap! cur start-editing))) title])))

(defn Card [card]
  [Editable {:className "card"} :div card])

(defn NewCard [col]
  [:div {:className "new-card"
         :on-click #(swap! col update :cards conj {:editing true}) } "+ add new card"])

(defn Column [col]
  [:div {:className "column"}
   [Editable {} :h2 col]
   (for [i (range (count (:cards @col)))]
     [Card (r/cursor col [:cards i])])
   [NewCard col]])

(defn NewColumn []
  [:div {:className "new-column"
         :on-click #(swap! app-state update :columns conj {:cards [] :editing true})} "+ add new column"])

(defn Board []
  [:div {:className "board"}
   (for [i (range (count (:columns @app-state)))]
     [Column (r/cursor app-state [:columns i])])
   [NewColumn]])

(r/render [Board] (js/document.getElementById "app"))
