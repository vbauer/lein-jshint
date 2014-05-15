(ns ^{:author "Vladislav Bauer"}
  lein-jshint.plugin
  (:require [lein-npm.plugin :as npm]
            [leiningen.jshint :as jshint]
            [leiningen.compile]
            [robert.hooke :as hooke]))


(def ^:private DEF_JSHINT_DEP "jshint")
(def ^:private DEF_JSHINT_VER ">=2.5.0")


; External API: Hooks

(defn compile-hook [task project & args]
  (apply task project args)
  (jshint/jshint project))

(defn activate []
  (npm/hooks)
  (hooke/add-hook #'leiningen.compile/compile #'compile-hook))


; External API: Middlewares

(defn- jshint? [dep]
  (= (str (first dep)) DEF_JSHINT_DEP))

(defn- find-jshint-deps [deps]
  (keep-indexed #(when (jshint? %2) %1) deps))

(defn- ensure-jshint [deps version]
  (let [jshint-matches (find-jshint-deps deps)
        new-dep [DEF_JSHINT_DEP (or version DEF_JSHINT_VER)]]
    (if (empty? jshint-matches)
      (conj deps new-dep) deps)))

(defn middleware [project]
  (let [version (get-in project [:jshint :version])]
    (update-in project [:node-dependencies]
               #(vec (ensure-jshint % version)))))
