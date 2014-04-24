(ns metarock.transform)

(defn- leveled-tree [s]
  (if (symbol? s)
    {:exp s :level 0}
    (let
        [args (map leveled-tree (rest s)) op (first s)]
      {:exp (cons op args) :level (+ (apply max (map :level args)) 1)})))

(declare delay-inserted-graph delay-inserted-arg)

(defn- delay-inserted-graph [s]
  (if (symbol? s) s
      (let [op (first s)
            max-level (apply max (map :level (rest s)))]
        (cons op (map #(delay-inserted-arg (:exp %) (- max-level (:level %))) (rest s))))))

(defn- delay-inserted-arg [s level]
  (if (= level 0)
    (delay-inserted-graph s)
    (list 'delay (delay-inserted-arg s (dec level)))))

(defn delay-insertion [s]
  (delay-inserted-graph (:exp (leveled-tree s))))


