(ns ^{:author "Vladislav Bauer"}
  lein-jshint.plugin
  (:require [lein-npm.plugin :as npm]
            [lein-jshint.core :as core]
            [leiningen.compile]
            [robert.hooke :as hooke]))


; Internal API: Configuration

(def ^:private DEF_JSHINT_DEP "jshint")
(def ^:private DEF_JSHINT_VER ">=2.9.6")


; Internal API: Middlewares

(defn- jshint? [dep]
  (= (str (first dep)) DEF_JSHINT_DEP))

(defn- find-jshint-deps [deps]
  (keep-indexed #(when (jshint? %2) %1) deps))

(defn- ensure-jshint [deps version]
  (let [jshint-matches (find-jshint-deps deps)
        new-dep [DEF_JSHINT_DEP (or version DEF_JSHINT_VER)]]
    (if (empty? jshint-matches)
      (conj deps new-dep) deps)))


; External API: Middlewares

(defn middleware [project]
  (let [version (get-in project [:jshint :version])]
    (update-in project [:node-dependencies]
               #(vec (ensure-jshint % version)))))


; External API: Hooks

(defn compile-hook [task project & args]
  (let [res (apply task project args)]
    (core/jshint project args)
    res))

(defn activate []
  (npm/hooks)
  (hooke/add-hook #'leiningen.compile/compile #'compile-hook))
