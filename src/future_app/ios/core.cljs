(ns future-app.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [future-app.events]
            [goog.object :as gobj]
            [future-app.subs]))

(def ReactNative (js/require "react-native"))
(def app-registry (gobj/get ReactNative "AppRegistry"))
(def text (r/adapt-react-class (gobj/get ReactNative "Text")))
(def view (r/adapt-react-class (gobj/get ReactNative "View")))
(def image (r/adapt-react-class (gobj/get ReactNative "Image")))
(def touchable-highlight (r/adapt-react-class (gobj/get ReactNative "TouchableHighlight")))

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title] (.alert (gobj/get ReactNative "Alert") title))

(defn app-root []
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} @greeting]
       [image {:source logo-img
               :style  {:width 80 :height 80 :margin-bottom 30}}]
       [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                             :on-press #(alert "HELLO!")}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press me"]]])))

(defn init []
      (dispatch-sync [:initialize-db])
      (.registerComponent app-registry "FutureApp" #(r/reactify-component app-root)))
