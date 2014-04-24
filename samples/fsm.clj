(def-mdoule inc
  (fn [n] (+ n 1)))


(def-fsm fsm1 [a b c]
  (do 
    (inc a)
    (inc b)
    (inc c)))

(def-fsm fsm2 [a b c]
  (par
   (inc a)
   (inc b)
   (inc c)))

(def

(def-fsm fsm3 [a b c]
  (do
    :init
    (inc a)
    (inc b)
    (if (= (inc c) 0) (goto :init))))
