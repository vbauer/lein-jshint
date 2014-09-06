(ns ^{:author "Vladislav Bauer"}
  leiningen.jshint
  (:require [leiningen.help :as help]
            [lein-jshint.core :as core]))

; External API: Task

(defn jshint
  "Invoke the JSHint, Static analysis tool for JavaScript"
  [project & args]
  (if (= args ["help"])
    (println (help/help-for core/DEF_JSHINT_CMD))
    (core/jshint project args)))
