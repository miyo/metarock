(ns metarock.operator)

(defrecord HDLModule [module-name architecture-type ports signals libraries])

(defn make-module
"make a new hdl module"
  [str]
  (->HDLModule str "RTL" '() '() '("ieee.std_logic_1164.all")))

(defn add-value-into-module-with-key!
  "Modifies the value of key into given new value"
  [mdl key value]
  (dosync
   (alter
    mdl
    (fn [m k v]
      (->HDLModule (:module-name m)
                   (:architecture-type m)
                   (if (#{:ports} k) (cons value (:ports m)) (:ports m))
                   (if (#{:signals} k) (cons value (:signals m)) (:signals m))
                   (if (#{:libraries} k) (cons value (:libraries m)) (:libraries m))))
    key value)))

(defn add-use-library!
  [m lib]
  (add-value-into-module-with-key! m :libraries lib))

(defn get-signal-width
  "Returns next valud of :width or default value (= 1)"
  [args]
  (let [i (index-filter #{:width} args)]
    (if (seq i)
      (nth args (inc (first i))) 1)))

(defrecord HDLSignal [name sign width debug])

(defn make-signal
  "make a new signal with name and options"
  [name & opt]
  (let [width (get-signal-width opt)
        sign (return-if-contains opt #{:unsigned :signed} #(first %) (fn [] :unsigned))
        debug (return-if-contains opt #{:debug} (fn [x] true) (fn [] false))]
    (->HDLSignal name sign width debug)))

(defrecord HDLPort [name dir sign width debug])

(defn make-port
  "make a new port with name, direction and options"
  [name dir & opt]
  (if (#{:in :out :inout} dir)
    (let [width (get-signal-width opt)
          sign (return-if-contains opt #{:unsigned :signed} #(first %) (fn [] :unsigned))
          debug (return-if-contains opt #{:debug} (fn [x] true) (fn [] false))]
      (->HDLPort name dir sign width debug))
    nil))
  
(defn add-signal!
  "Adds a signal into the module [m]"
  [m s]
  (if (and (not (nil? s)) (= (class s) HDLSignal))
    (add-value-into-module-with-key! m :signals s)
    (do
      (println "Not a signal is given. Module has not been modifed.")
      m
      )))

(defn add-port!
  "Adds a port into the module [m]"
  [m p]
  (if (and (not (nil? p)) (= (class p) HDLPort))
    (add-value-into-module-with-key! m :ports p)
    (do
      (println "Not a port is given. Module has not been modifed.")
      m)))

(defn add-use-library
"add libraries required in this module"
 [m, &str]
;; alter conj   
  )

(defn add-statement
"add a statement into the module [m]"
  [m s &opt]
  )

(defn assign-statement
"make a new assign statement, in which [dest] is destination signal/port and [expr] is expression"
  [dest expr &opt]
  )

(defn add-preocess
"add aprocess into the module [m]"
  [m p &opt]
  )

(defn make-sequential-process
"make a new sequential process"
  [m params reset-stmt-list body-stmt-list &opt]
  )

(defn clear-statement
"make a statement to clear the target signal. ex name <= (others =>'0'); "
  [name]
  )

(defn increment-statement
"make a statement to increment the target signal. ex name <= name + 1; "
  [name]
  )

(defn index-ref
"index refference.
When idx0 is given only, that means 1-bit reffernce. ex. name[idx0].
When idx0 and idx1, that means range reffernce. ex. name[idx0 downto idx1].
"
  [name idx0 &idx1 &opt]
  
  )

(defn vhdl-raw-stmt
"return strings as parts of VHDL source code"
  [str]
  )


(defn optimize
  [m]
  )

(defn validate
  [m]
  )

(defn graphize
  [m]
  )

(defn generate
  [m kind]
  (cond (#{:verilog} kind) (generate-verilog @m)
        (#{:vhdl} kind) (generate-vhdl @m)
        true (println @m)))

