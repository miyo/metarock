(ns mhdl.core)

(defn define-module ;; マクロじゃないとだめ
  [name func]
  {:name name :func func})

