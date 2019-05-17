(ns moody.core
  (:require [moody.data_prep :as prep]
            [moody.analyze :as analyze]
            [moody.tmdb_client :as client]
            [clojure.string :as string]))

(def default_training_file "resources/training.csv")
(def default_test_file "resources/test.csv")
(def default_sentiment_map {"0" :negative "1" :positive})
(def punctuation_regex #"(?<=[.!?]|[.!?][\\'\"])(?<!e\.g\.|i\.e\.|vs\.|p\.m\.|a\.m\.|Mr\.|Mrs\.|Ms\.|St\.|Fig\.|fig\.|Jr\.|Dr\.|Prof\.|Sr\.|[A-Z]\.)\s+")
(def bag (prep/train default_training_file default_sentiment_map 2))

(defn get-score
  "Gets the predicted percentage score of a movie by categorizing the reviews
    by predicted sentiment, then returning the (number of positive reviews)/total reviews."
  [title]
  (let [movie_id (client/get-movie-id title)
        reviews (map #(get % "content") (client/get-reviews movie_id))
        sentences (reduce concat (map #(string/split % punctuation_regex) reviews))
        results (map #(analyze/nb-classifier bag default_sentiment_map %) sentences)]
    (/ (count(filter #(= :positive %) results)) (count results))
    )
  )

(defn -main
  [& args]
  (if-not (empty? args)
    (println (str (* 100 (float (get-score (string/join " " args)))) "%"))
    (println ("Please supply a movie name."))
    ))



