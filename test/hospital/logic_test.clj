(ns hospital.logic_test
  (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [hospital.logic :refer :all]
            [hospital.model :refer :all]
            [schema.core :as s]))



(s/set-fn-validation! true)


(deftest cabe-na-fila?-test
  (testing "Que cabe numa fila vazia"
    (is (cabe-na-fila? {:espera []} :espera)))

  (testing "Que não cabe na fila quando a fila está cheia"
    (is (not (cabe-na-fila? {:espera [1 5 37 54 21]}, :espera))))

  (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
    (is (not (cabe-na-fila? {:espera [1 5 6 74 2 4]} :espera))))

  (testing "Que cabe na fila quando tem gente mas não está cheia"
    (is (cabe-na-fila? {:espera [2 45 6 4]} :espera))
    (is (cabe-na-fila? {:espera [3 4]} :espera)))

  (testing "Que não cabe quando o departamento não existe"
    (is (not (cabe-na-fila? {:espera [1 3 4]} :raio-x))))
  )


(deftest chega-em-test
  (let [hospital-cheio {:espera [1 35 42 64 21]}]

    (testing "aceita pessoas enquanto cabem pessoas na fila"
      (is (= {:espera [1, 2, 5]}
             (chega-em {:espera [1, 2]} :espera, 5)))
      (testing "não aceita quando não cabe na fila"
        (is (thrown? clojure.lang.ExceptionInfo
                     (chega-em hospital-cheio :espera 76)))
        ))))

(deftest transfere-test
  (testing "aceita pessoas se cabe"
    (let [hospital-original {:espera (conj hospital.model/fila-vazia "5")
                             :raio-x hospital.model/fila-vazia}]
      (is (= {:espera []
              :raio-x ["5"]}
             (transfere hospital-original :espera :raio-x)))
      )

    (let [hospital-original {:espera (conj hospital.model/fila-vazia "51" "5")
                             :raio-x (conj hospital.model/fila-vazia "13")}]
      (pprint (transfere hospital-original :espera :raio-x))
      (is (= {:espera ["5"]
              :raio-x ["13" "51"]}
             (transfere hospital-original :espera :raio-x)))
      ))
  (testing "recusa pessoas se não cabe"
    (let [hospital-cheio {:espera (conj hospital.model/fila-vazia "5")
                          :raio-x (conj hospital.model/fila-vazia "1" "54" "43" "12" "51")}]
      (is (thrown? clojure.lang.ExceptionInfo
                   (transfere hospital-cheio :espera :raio-x)))
      )))
