(ns moody.data_prep_test
  (:require [clojure.test :refer :all]
            [moody.data_prep :refer :all]))


(deftest tokenizeTestWhereNis1
  (testing "String tokenization with n of 1"
           (let [sentence "THIS5 IS A TEST SENTENCE !...?/"
                 answer ["this" "is" "a" "test" "sentence"]]
             (is (= (tokenize-sentence sentence 1) answer)))
           ))

(deftest tokenizeTestWhereNis2
  (testing "String tokenization with n of 1"
           (let [sentence "THIS5 IS A TEST SENTENCE !...?/"
                 answer ["this is" "a test" "sentence"]]
             (is (= (tokenize-sentence sentence 2) answer)))
           ))