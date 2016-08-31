(ns word-jumbler.core)
(require '[clojure.string :as str])

;; split the whole text into words
(defn split-text [x]
  (str/split x #" "))

;; test
(split-text "Clojure is awesome")
;; => ["Clojure" "is" "awesome"]

;; shuffle all characters in a word except for the first and last ones
(defn jumble-inner [x]
  (apply str (shuffle (rest (drop-last x)))))

;; test
(jumble-inner "brooms")
;; => "ormo"

;; jumble one word, add space at the end
(defn jumble-word [word]
  (str (first word) (jumble-inner word) (last (rest word)) " "))

;; test
(jumble-word "Clojure")
;; => "Corjlue "

;; jumble the whole text, handle one-character words
(defn jumble-sentence [sentence]
  (apply str (drop-last (apply str (map jumble-word (split-text sentence))))))

;; tests
(jumble-sentence "Clojure is awesome")
;; => "Cjrloue is asowmee"
(jumble-sentence "Clojure s awesome")
;; => "Culorje s aosewme"



;; replace vowels with underscores
(defn replace-vowels [x]
  (str/replace x #"[aeiou]" "_"))

;; tests
(replace-vowels "the color is red")
;; => "th_ c_l_r _s r_d"
(replace-vowels (jumble-sentence "Clojure is awesome"))
;; => "Clj__r_ _s ___wsm_"

;; extract and shuffle vowels
(defn shuffle-vowel-list [text]
  (shuffle (re-seq #"[aeiou]" text)))

;; test
(shuffle-vowel-list "Clojure is awesome")
;; => ["i" "o" "a" "e" "e" "e" "u" "o"]

;; insert all the vowels from the shuffled list
(defn insert-vowels [text vowel-list]
  (if (empty? vowel-list)
    text
    (insert-vowels (str/replace-first text #"[_]" (first vowel-list)) (rest vowel-list))))

;; put it all together (leave consonants in places, shuffle vowels only)
(defn shuffle-vowels [text]
  (insert-vowels (replace-vowels text) (shuffle-vowel-list text)))

;; test
(shuffle-vowels "Clojure is awesome")
;; => "Clejure es owisamo"

;; another way to join strings into text
(str/join " "(map shuffle-vowels (split-text "Clojure is awesome")))




