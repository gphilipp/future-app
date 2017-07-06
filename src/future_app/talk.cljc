(ns future-app.talk
  #?(:cljs (:require-macros [cljs.core.async.macros :refer [go-loop go]]))
  (:require
    #?@(:clj [[clojure.data.json :as json]
              [clojure.pprint :as pprint]])
    #?@(:cljs [[cljs.core.async :refer [<! >! chan take! put!]]
               [cljs-http.client :as http]
               [cljs.pprint :as pprint]])
              [clojure.spec.alpha :as spec]
              [clojure.spec.gen.alpha :as gen]
              [clojure.spec.test.alpha :as stest])
  (:import #?(:clj (java.util Date))))


#?(:clj  (type (Date.))
   :cljs (js/alert "foo"))



;┌───────────────────────────────────────────┐
;│                                           │
;│          Les données littérales           │
;│                                           │
;└───────────────────────────────────────────┘


;; strings
"Clojure rocks"

;Expressions régulières
(re-find #"^Clojure" "Clojure rocks")

(type "Clojure rocks")

;; numbers
42

;; decimals
0.07

#inst"2017-07-04T20:18:31.861-00:00"

;; symbols
'dwayne

;; keywords
:age

;; precise arithmetic (in Clojure, not in ClojureScript)
(/ 1 3)

;┌───────────────────────────────────────────┐
;│                                           │
;│        Les collections littérales         │
;│                                           │
;└───────────────────────────────────────────┘


;; collections
{:firstname "Ellen" :lastname "Ripley"}
#{"a" "set" "of" "words"}

;; functions
(defn greeter [name] (println "Greetings," name))
(greeter "Boromir")


;; nested collections
(def aliens-characters [{:firstname "Ellen"
                         :lastname "Ripley"
                         :rank "Officer"
                         :guns [{:type :flamethrower :model "M240A1"}
                                {:type :pulse-rifle :model "M41A"}]}
                        {:firstname "Mark"
                         :lastname "Drake"
                         :rank "Private First Class"
                         :guns [{:type :smartgun :model "M56"}
                                {:type :knife :length 10}]}])

(get-in aliens-characters [1 :guns 0 :type])

(def with-ripley-demoted (assoc-in aliens-characters [0 :rank] "Civilian"))

(pprint/print-table aliens-characters)


#?(:clj (defn parse-json [resource-name]
          (json/read-json (slurp resource-name)))
   #_(:cljs (defn parse-json [resource-name]
              (.then (js/fetch resource-name)
                (fn [r]
                  (.then (.text r)
                    (fn [text]
                      (println text))))))))


;#?(:clj  (def resource "resources/aliens.json")
;   :cljs (def resource "https://raw.githubusercontent.com/Parjure/clojure-talk/master/resources/aliens.json"))
;(parse-json resource)
;
;
;(go (prn (<! (http/get resource))))
;(def foo (take! (http/get resource) identity))


#?(:clj  (def aliens-character-from-json (parse-json "resources/aliens.json"))
   :cljs (def aliens-character-from-json (js->clj (js/JSON.parse "[\n  {\n    \"firstname\": \"Ellen\",\n    \"lastname\": \"Ripley\",\n    \"rank\": \"Officer\",\n    \"guns\": [\n      {\n        \"type\": \"flamethrower\",\n        \"model\": \"M240A1\"\n      },\n      {\n        \"type\": \"pulse-rifle\",\n        \"model\": \"M41A\"\n      }\n    ]\n  },\n  {\n    \"firstname\": \"Mark\",\n    \"lastname\": \"Drake\",\n    \"rank\": \"Private\",\n    \"guns\": [\n      {\n        \"type\": \"smartgun\",\n        \"model\": \"M56\"\n      },\n      {\n        \"type\": \"knife\",\n        \"length\": \"10\"\n      }\n    ]\n  }\n  ,  {\n  \"firstname\": \"Jenette\",\n  \"lastname\": \"Vasquez\",\n  \"rank\": \"Private\",\n  \"guns\": [\n    {\n      \"type\": \"smartgun\",\n      \"model\": \"M56\"\n    },\n    {\n      \"type\": \"knife\",\n      \"length\": \"20\"\n    }\n  ]\n}\n\n]\n")
                                           :keywordize-keys true)))

(map :guns aliens-character-from-json)

(defn has-weapon
  [weapon character]
  (let [guns (:guns character)]
    (some #(= (:type %) weapon) guns)))

(has-weapon "knife" (second aliens-character-from-json))


(->> aliens-character-from-json
  (filter (partial has-weapon "knife"))
  (map :lastname))


;┌───────────────────────────────────────────┐
;│               CLojure Spec                │
;│      Permet de spécifier ses données      │
;│                                           │
;└───────────────────────────────────────────┘


(spec/def :gun/type #{"flamethrower" "powerloader" "smartgun" "pulse-rifle" "knife" "grenade" "shotgun"})
(spec/def :gun/model string?)
(spec/def :gun/length (spec/int-in 10 200))


(spec/def :crew/gun (spec/keys :req-un [:gun/type]
                               :opt-un [:gun/model
                                        :gun/length]))


(spec/def :crew/firstname string?)
(spec/def :crew/lastname string?)
(spec/def :crew/rank #{"private" "lieutenant" "sergeant" "Officer" "commander"})
(spec/def :crew/guns (spec/coll-of :crew/gun :min-count 1 :max-count 3 :distinct true))

(spec/def :crew/civilian (spec/keys :req-un [:crew/firstname
                                             :crew/lastname
                                             ]))

(spec/def :crew/soldier (spec/merge :crew/civilian
                                    (spec/keys :req-un [:crew/rank
                                                        :crew/guns])))


(spec/def :crew/member (spec/or :civilian :crew/civilian
                             :soldier :crew/soldier))

;┌───────────────────────────────────────────┐
;│               CLojure Spec                │
;│       Permet de valider ses données       │
;│                                           │
;└───────────────────────────────────────────┘

(spec/def :crew/squad (spec/coll-of :crew/soldier))

(spec/conform :crew/squad aliens-character-from-json)

(spec/explain :crew/squad aliens-character-from-json)


;┌───────────────────────────────────────────┐
;│               CLojure Spec                │
;│      Permet de spécifier ses données      │
;│                                           │
;└───────────────────────────────────────────┘


(gen/sample (spec/gen :crew/member) 25)




;┌───────────────────────────────────────────┐
;│               CLojure Spec                │
;│    Permet de spécifier le contrat des     │
;│                 fonctions                 │
;└───────────────────────────────────────────┘


(spec/fdef has-weapon
        :args (spec/cat :gun :gun/type :person :crew/soldier))

(spec/exercise-fn `has-weapon 25)


