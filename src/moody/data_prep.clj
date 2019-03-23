(ns moody.data_prep
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.pprint :as pprint]))

(defn deep-merge-with
  "Like merge-with, but merges maps recursively, applying the given fn
  only when there's a non-map at a particular level.
  (deep-merge-with + {:a {:b {:c 1 :d {:x 1 :y 2}} :e 3} :f 4}
                     {:a {:b {:c 2 :d {:z 9} :z 3} :e 100}})
  -> {:a {:b {:z 3, :c 3, :d {:z 9, :x 1, :y 2}}, :e 103}, :f 4}"
  [f & maps]
  (apply
   (fn m [& maps]
     (if (every? map? maps)
       (apply merge-with m maps)
       (apply f maps)))
   maps))

(defn tokenize-sentence
  "Splits a sentence into tokens where n is the number of words in each token"
  [sentence n]
  (vec (map #(string/join " " (vec %)) (partition n n nil
    (string/split
     (string/lower-case (apply str (filter #(or (Character/isLetter %) (Character/isSpaceChar %)) sentence)))
      #" ")))))

(defn count-words
  "Outputs a frequency map of terms in a tokenized sentence"
  [tokenized_sentence]
  (frequencies tokenized_sentence))

(defn word-maps

  [words sentiment_map sentiment]
  (let [default_map (reduce #(assoc %1 %2 0) {} (vals sentiment_map))]
    (map #(assoc {} % (assoc default_map sentiment (get words %))) (keys words))
    )

  )

(defn gram-count
  [data_row sentiment_map n]
  (let [sentiment (get sentiment_map (first data_row))
        words (count-words (tokenize-sentence (last data_row) n))]
    (merge (reduce merge (word-maps words sentiment_map sentiment)) {sentiment 1 :num-docs 1} )
    ))

(defn train
  "generates a vocab/bag of words where n is the number of words per term.
  Each word is mapped to a map of sentiments mapped to the number of times that word
  appears in a sentence with that sentiment."
  [file sentiment_map n]
   (with-open [reader (io/reader file)]
     (assoc (->>
      (csv/read-csv reader)
      (map #(gram-count % sentiment_map n))
      (reduce #(deep-merge-with + %1 %2))
      (doall)) :token_size n)
     )
  )

(defn display
  [data]
  (pprint/pprint data))
