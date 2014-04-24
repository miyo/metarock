(def-sp add [x y] (+ x y))

(def-sp sub [x y] (- x y))

(def-sp mult [x y] (* x y))

(def-sp mac [x y z] (+ (* x y) z))

(def-sp mac2 [x y z] (add (mult x y) z))

(def-sp longadd [a b c d e f g]
  (+ a (+ b (+ c (+ d (+ e (+ f g)))))))

(def-sp longadd2 [a b c d e f g h]
  (+ a (+ b c) (+ d (+ e (+ f g))) h))

(def-sp mux [s a b] (if (= s 0) a b))

;; (def-rule rule1 [x y]
;;   'always
;;   (
;;    (add x y)
;;    (sub x y)))

;; (def-rule rule2 [x y]
;;   (> x 1)
;;   (par
;;    (add x y)
;;    (sub x y)))

