(ns mhdl.visualize
  (:require clojure.core))

(defn read-as-sexp [path]
  (read-string (str "(" (slurp path) ")")))

(defn parse-module [s]
  (let [get-module-name (fn [x] (first x))
        get-module-args (fn [x] (first (rest (first (rest x)))))
        get-module-body (fn [x] (first (rest (rest (first (rest s))))))]
    {:name (get-module-name s)
     :args (get-module-args s)
     :body (get-module-body s)}))

(defn parse-sexp [s]
  (let [x (first s)]
    (if (= x 'def-module)
      (parse-module (rest s))
      nil)))

(declare gen-dot-symbol gen-dot-graph gen-dot-edge gen-dot-edge-0)

(defn gen-dot-symbol [s args]
  (do
    (if (some #(= % s) args)
      (println (str "\"" s "\"" "[shape = box, peripheries = 2];"))
      (println (str "\"" s "\"" "[shape = box];")))
    {:name s :level 0}))

(defn gen-dot-graph [s args]
  (if (symbol? s)
    (gen-dot-symbol s args)
    (gen-dot-edge s args)))

(defn gen-dot-edge-0 [a dest level]
  (if (= (:level a) level)
    (println (str "\"" (:name a) "\"" " -> " "\"" dest "\"" ";"))
    (do
      (println (str "\"" (:name a) "\"" " -> " "\"" (str "FF-" (:name a)) "\"" ";"))
      (println (str "\"" (str "FF-" (:name a)) "\"" " -> " "\"" (str (:name a) 'z) "\"" ";"))
      (println (str "\"" (:name a) 'z "\"" "[shape = box];"))
      (gen-dot-edge-0 {:name (str (:name a) 'z) :level (+ (:level a) 1)} dest level))))

(defn gen-dot-edge [s args]
  (let
      [op (gensym (str "op-" (first s) "-"))
       result (gensym (str "result-" (first s) "-"))
       inputs (map (fn [a] (gen-dot-graph a args)) (rest s))
       ]
    (let [level (apply max (map :level inputs))]
      (do
        (dorun
         (map (fn [a]
                (if (= (:level a) level)
                  (println (str "\"" (:name a) "\"" " -> " "\"" op "\"" ";"))
                  (gen-dot-edge-0 a op level))) inputs))
        (println (str "\"" result "\"" "[shape = box];"))
        (println (str "\"" op "\"" "->" "\"" result "\"" ";"))
        {:name result :level (+ level 1)}))))

(defn gen-dot [name sexp args]
  (do
    (println (str "digraph " name "{"))
    (println (str "graph [label = " "\"" name "\"" "]"))
    (gen-dot-graph sexp args)
    (println "}")))

;;
;; (def x (first (rest (rest (map parse-sexp (read-as-sexp "samples/arith.clj"))))))
;; (gen-dot (:name x) (:body x) (:args x))
;;
;; (def y (first (rest (rest (map parse-sexp (read-as-sexp "samples/arith.clj"))))))
;; (gen-dot (:name y) (:body y) (:args y))
;;
