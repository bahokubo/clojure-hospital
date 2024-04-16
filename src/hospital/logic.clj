(ns hospital.logic)


(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
          departamento
          count
          (< 5)))

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    ;perguntar
    (update hospital departamento conj pessoa)
    (throw (ex-info "Não cabe ninguém neste departamento" {:paciente pessoa}))))

(defn atende
  [hospital departamento]
  (update hospital departamento pop))

(defn proximo
  "Retorna o próximo paciente da fila"
  [hospital departamento]
  (-> hospital
      departamento
      peek))

(defn transfere
  "Transfere o próximo paciente da fila de para a fila para"
  [hospital de para]
  (let [pessoa (proximo hospital de)]
  (-> hospital
      (atende de)
      (chega-em para pessoa))))
