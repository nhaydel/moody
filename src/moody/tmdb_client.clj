(ns moody.tmdb_client
  (:require [clj-http.client :as client]
            [clojure.string :as string]
            [clojure.data.json :as json]))

(def api_key (string/trim (slurp "./resources/API_KEY")))
(def base_url "https://api.themoviedb.org/3/")
(def language "&language=en-US&")

(defn get-title
  [title]
  (string/replace title #" " "%20")
  )

(defn get-movie-id
  [title]
  (->
   (str base_url "search/movie?api_key=" api_key language "query=" (get-title title) "&page=1&include_adult=false")
   (client/get {:max-redirects 1 :redirect-strategy :graceful :accept :json})
   (:body)
   (json/read-str)
   (get "results")
   (get 0)
   (get "id")
   ))

(defn get-reviews
  [movie_id]
  (->
   (str base_url "movie/" movie_id "/reviews?api_key=" api_key language "&page=1")
   (client/get {:max-redirects 1 :redirect-strategy :graceful :accept :json})
   (:body)
   (json/read-str)
   (get "results")
  ))