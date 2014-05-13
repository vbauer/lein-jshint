(ns ^{:author "Vladislav Bauer"}
  leiningen.jshint
  (:require [leiningen.npm :as npm]
            [leiningen.npm.process :as process]
            [leiningen.core.main :as main]
            [leiningen.help :as help]
            [leiningen.compile]
            [cheshire.core :as json]
            [robert.hooke :as hooke]
            [org.satta.glob :as glob]
            [clojure.java.io :as io]
            [clojure.string :as string]))

; Internal API: Common

(defn- find-files [patterns]
  (map str (flatten (map glob/glob patterns))))

(defn- create-tmp-file [file content]
  (doto (io/file file)
    (spit content)
    (.deleteOnExit)))

(defn joine [& data]
  (string/join "\r\n" data))


; Internal API: Configuration

(def ^:private DEF_CONFIG_FILE ".jshintrc")
(def ^:private DEF_IGNORE_FILE ".jshintignore")
(def ^:private DEF_CONFIG
  {:bitwise    true    ; Prohibit bitwise operators (&, |, ^, etc.)
   :curly      true    ; Require {} for every new block or scope
   :eqeqeq     true    ; Require triple equals i.e. ===
   :forin      true    ; Tolerate "for in" loops without hasOwnPrototype
   :immed      true    ; Require immediate invocations to be wrapped in parens
   :latedef    true    ; Prohibit variable use before definition
   :newcap     true    ; Require capitalization of all constructor functions
   :noarg      true    ; Prohibit use of arguments.caller and arguments.callee
   :noempty    true    ; Prohibit use of empty blocks
   :nonew      true    ; Prohibit use of constructors for side-effects
   :plusplus   true    ; Prohibit use of "++" & "--"
   :regexp     true    ; Prohibit "." and ""[^...]"" in regular expressions
   :undef      true    ; Require all non-global vars be declared before usage
   :strict     true    ; Require "use strict" pragma in every file
   :trailing   true    ; Prohibit trailing whitespaces
   })


(defn- opt [project k v] (get-in project [:jshint k] v))

(defn- config [project] (opt project :config {}))
(defn- config-file [project] (opt project :config-file DEF_CONFIG_FILE))
(defn- ignore-file [project] (opt project :ignore-file DEF_IGNORE_FILE))
(defn- include-files [project] (find-files (vec (opt project :includes nil))))
(defn- exclude-files [project] (find-files (vec (opt project :excludes nil))))


; Internal API: Runner

(defn- generate-config-file [project]
  (json/generate-string
   (merge (config project) DEF_CONFIG)))

(defn- generate-exclude-files [project]
  (let [excludes (exclude-files project)
        content (joine excludes)]
    (create-tmp-file (ignore-file project) content)))

(defn- sources-list [project args]
  (let [includes (include-files project)
        excludes (exclude-files project)]
    (remove empty? (concat (apply vec args) includes))))

(defn- invoke [project & args]
  (process/exec
   (project :root)
   (cons "jshint" args)))

(defn- proc [project & args]
  (try
    (npm/environmental-consistency project)
    (let [file (config-file project)
          content (generate-config-file project)
          sources (sources-list project args)]
      (npm/with-json-file file content project
                          (generate-exclude-files project)
                          (apply invoke project sources)))
    (catch Throwable t
      (println (joine (str "Can't execute jshint application: " (.getMessage t))
                      "Check that JSHint is:"
                      " - installed correctly: npm install jshint -g"
                      " - configured correctly: https://github.com/vbauer/lein-jshint.git"))
      (main/abort))))


; External API: Task

(defn jshint
  "Invoke the JSHint, Static analysis tool for JavaScript"
  [project & args]
  (if (= args ["help"])
    (println (help/help-for "jshint"))
    (proc project args)))


; External API: Hooks

(defn check-hook [f & args]
  (apply f args)
  (proc (first args)))

(defn activate []
  (hooke/add-hook #'leiningen.compile/compile #'check-hook))
