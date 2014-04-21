(def-module add
  (fn [x y] (+ x y)))

(def-module sub
  (fn [x y] (- x y)))

(def-module mac
  (fn [x y z] (* (+ x y) z)))

(def-module longadd
  (fn [a b c d e f g] (+ a (+ b (+ c (+ d (+ e (+ f g))))))))

