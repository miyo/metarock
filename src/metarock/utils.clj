(ns metarock.utils)

(defn indexed [col] (map-indexed vector col))

(defn index-filter
  [pred col]
  (when pred 
    (for [[idx elt] (indexed col) :when (pred elt)] idx)))

(defn insert-into-string-list
  [s l]
  (rest (flatten (map (fn [x y] [x y]) (repeat s) l))))

(defn return-if-contains
  "Returns evaluation result of then-form when args contains some of keywords-set,
or evaluation result of else-form"
  [args keywords-set then-form else-form]
  (if-let [a (seq (filter keywords-set args))] (then-form a) (else-form)))
