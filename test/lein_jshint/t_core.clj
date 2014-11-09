(ns ^{:author "Vladislav Bauer"}
  lein-jshint.t-core
  (:use [midje.sweet :only [fact]]
        [midje.util :only [testable-privates]])
  (:require [lein-jshint.core]))


; Configurations

(testable-privates
  lein-jshint.core
    jshint)

(def ^:private DEF_CONFIG
  {:jshint
    {:debug true
     :includes "example/resources/*.js"}})


; Tests

(fact "Check JSHint processor"
  (nil? (jshint DEF_CONFIG)) => false
  (nil? (jshint DEF_CONFIG ["-verbose"])) => false)
