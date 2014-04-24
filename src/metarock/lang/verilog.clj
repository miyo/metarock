(ns metarock.lang.verilog
  (:use metarock.utils)
  )

(defn generate-verilog-port-decl
  [port]
  (let [d ((fn [x] (cond (#{:in} x) "input", (#{:out} x) "output", (#{:inout} x) "inout", true "input")) (:dir port))
        t ((fn [w] (if (= w 1) "" (format "[%d:%d] " (- w 1) 0))) (:width port))]
    (format "%s wire %s%s" d t (:name port))))

(defn generate-verilog-ports-list
  [ports]
  (apply str (insert-into-string-list ",\n" (map (fn [x] (generate-verilog-port-decl x)) ports))))

(defn generate-verilog-signal-decl
  [sig]
  (let [t ((fn [w] (if (= w 1) "reg" (format "reg[%d:%d]" (- w 1) 0))) (:width sig))]
    (format "%s %s;\n" t (:name sig))))

(defn generate-verilog-signals-list
  [signals]
  (apply str (map (fn [x] (generate-verilog-signal-decl x)) signals)))

(defn generate-verilog
  [m]
  (do
    (println
     (format "module %s (\n" (:module-name m))
     (generate-verilog-ports-list (:ports m))
     (format "\n);\n")
     "\n"
     (generate-verilog-signals-list (:signals m))
     (format "end module; // %s" (:module-name m)))))

