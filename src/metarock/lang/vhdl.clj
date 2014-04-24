(ns metarock.lang.vhdl
  (:use metarock.utils)
  )

(defn generate-vhdl-port-decl
  [port]
  (let [d ((fn [x] (cond (#{:in} x) "in", (#{:out} x) "out", (#{:inout} x) "inout", true "in")) (:dir port))
        t ((fn [w] (if (= w 1) "std_logic" (format "std_logic_vector(%d downto %d)" (- w 1) 0))) (:width port))]
    (format "%s : %s %s" (:name port) d t)))

(defn generate-vhdl-ports-list
  [ports]
  (apply str (insert-into-string-list ";\n" (map (fn [x] (generate-vhdl-port-decl x)) ports))))

(defn generate-vhdl-signal-decl
  [sig]
  (let [t ((fn [w] (if (= w 1) "std_logic" (format "std_logic_vector(%d downto %d)" (- w 1) 0))) (:width sig))]
    (format "signal %s : %s;\n" (:name sig) t)))

(defn generate-vhdl-signals-list
  [signals]
  (apply str (map (fn [x] (generate-vhdl-signal-decl x)) signals)))

(defn generate-vhdl
  [m]
  (do
    (println
     (format "entity %s is\n" (:module-name m))
     (format "port(\n")
     (generate-vhdl-ports-list (:ports m))
     (format ");\n")
     (format "end %s;\n" (:module-name m))
     (format "architecture %s of %s is\n" (:architecture-type m) (:module-name m))
     (generate-vhdl-signals-list (:signals m))
     (format "begin\n")
     (format "end %s;" (:architecture-type m)))))

