(ns ^{:author "Vladislav Bauer"}
  lein-jshint.t-core
  (:require [lein-jshint.core :as core]
            [clojure.test :as t]))


; Configurations

(def ^:private DEF_CONFIG
  {:jshint
    {:debug true
     :includes "example/resources/*.js"}})

(def ^:private not-nil? (complement nil?))


; Tests

(t/deftest check-jshint-processor
  (t/is (not-nil? (core/jshint DEF_CONFIG)))
  (t/is (not-nil? (core/jshint DEF_CONFIG ["-verbose"]))))
