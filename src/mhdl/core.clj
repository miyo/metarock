(ns mhdl.core
  (:require clojure.core))

(defn read-as-sexp [path]
  (read-string (str "(" (slurp path) ")")))

(defmacro def-sp [name args body]
  `{:name (quote ~name)
   :args (quote ~args)
   :body (quote ~body)})
