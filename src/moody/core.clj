(ns moody.core
  (:require [moody.data_prep :as prep]
            [moody.analyze :as analyze]
            [moody.tmdb_client :as client]))

(def default_training_file "resources/training.csv")
(def default_test_file "resources/test.csv")
(def default_sentiment_map {"0" :negative "1" :positive})

(defn -main
  "Optionally train a machine learning algorithm implementation, then take "
  [& args]
  (def bag (prep/train default_training_file default_sentiment_map 1))
  (println (analyze/nb_classifier bag "this is good" default_sentiment_map))
  (def captain_marvel_id (client/get-movie-id "Captain Marvel"))
  (println captain_marvel_id)
  )



