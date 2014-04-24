(ns mhdl.visualize)

(declare gen-dot-symbol gen-dot-graph gen-dot-edge gen-dot-edge-0)

(defn- make-box [s & opt]
  (if (some #(= % 'double) opt)
    (println (str "\"" s "\"" "[shape = box, peripheries = 2];"))
    (println (str "\"" s "\"" "[shape = box];"))))

(defn- print-dot-edge [src dst]
  (println (str "\"" src "\"" " -> " "\"" dst "\"" ";")))

(defn- gen-dot-symbol [s args]
  (do
    (make-box s (if (some #(= % s) args) 'double 'single))
    s))

(defn- gen-dot-graph [s args]
  (if (or (symbol? s) (number? s))
    (gen-dot-symbol s args)
    (gen-dot-edge s args)))

(defn- gen-dot-edge [s args]
  (let
      [op (gensym (str "op-" (first s) "-"))
       result (gensym (str "result-" (first s) "-"))
       inputs (map #(gen-dot-graph % args) (rest s))
       ]
    (do
      (dorun (map #(print-dot-edge % op) inputs))
      (make-box result)
      (print-dot-edge op result)
      result)))

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
