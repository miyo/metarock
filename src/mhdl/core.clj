(ns mhdl.core
  (:require clojure.core))

(defn parse-module [s]
  (let [get-module-name #(first %)
        get-module-args #(first (rest (first (rest %))))
        get-module-body #(first (rest (rest (first (rest %)))))]
    {:name (get-module-name s)
     :args (get-module-args s)
     :body (get-module-body s)}))

(defn parse-sexp [s]
  (let [x (first s)]
    (if (= x 'def-module)
      (parse-module (rest s))
      nil)))

(defn read-as-sexp [path]
  (read-string (str "(" (slurp path) ")")))

