(ns ^{:author "Vladislav Bauer"}
  lein-jshint.core
  (:require [leiningen.npm :as npm]
            [leiningen.npm.process :as process]
            [leiningen.core.main :as main]
            [cheshire.core :as json]
            [me.raynes.fs :as fs]
            [clojure.java.io :as io]
            [clojure.string :as string]))


; Internal API: Common

(defn- abs-path [f] (.getAbsolutePath f))

(defn- clean-path [p]
  (if (not (nil? p))
    (if (.startsWith (System/getProperty "os.name") "Windows")
      (string/replace p "/" "\\")
      (string/replace p "\\" "/"))))

(defn- scan-files [patterns]
  (set (map abs-path (mapcat fs/glob (map clean-path patterns)))))

(defn- create-tmp-file [file content]
  (doto (fs/file file)
    (spit content)
    (.deleteOnExit)))

(defn joine [& data]
  (string/join "\r\n" data))

(defn- error [ex dbg]
  (if dbg (.printStackTrace ex))
  (println
   (joine "\r\n"
          (str "Can't execute JSHint: " (.getMessage ex))
          "Something is wrong:"
          " - installation: npm install jshint -g"
          " - configuration: https://github.com/vbauer/lein-jshint")))

(defn- root [project]
  (if-let [root (project :npm-root)]
    (if (keyword? root)
      (project root) ;; e.g. support using :target-path
      root)
    (project :root)))


; Internal API: JSON

(defn- json-file
  [filename project]
  (io/file (root project) filename))

(defn write-json-file
  [filename content project]
  (doto (json-file filename project)
    (spit content)
    (.deleteOnExit)))

(defn remove-json-file
  [filename project]
  (.delete (json-file filename project)))

(defmacro with-json-file
  [filename content project & forms]
  `(try
     (write-json-file ~filename ~content ~project)
     ~@forms
     (finally (remove-json-file ~filename ~project))))


; Internal API: Configuration

(def ^:public DEF_JSHINT_CMD "jshint")

(def ^:private DEF_JSHINT_DIR "node_modules/jshint/bin/")
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
(defn- debug [project] (opt project :debug false))
(defn- config-file [project] (opt project :config-file DEF_CONFIG_FILE))
(defn- ignore-file [project] (opt project :ignore-file DEF_IGNORE_FILE))
(defn- include-files [project] (scan-files (opt project :includes [])))
(defn- exclude-files [project] (scan-files (opt project :excludes [])))


; Internal API: Runner

(defn- generate-config-file [project]
  (json/generate-string
   (merge (config project) DEF_CONFIG)))

(defn- generate-exclude-files [project]
  (let [excludes (exclude-files project)
        content (joine excludes)]
    (create-tmp-file (ignore-file project) content)))

(defn- invoke [project args]
  (let [root (:root project)
        local (str DEF_JSHINT_DIR DEF_JSHINT_CMD)
        cmd (if (.exists (fs/file local)) local DEF_JSHINT_CMD)]
    (process/exec root (cons cmd args))))


; External API: Runner

(defn jshint [project & args]
  (try
    (npm/environmental-consistency project)
    (let [file (config-file project)
          content (generate-config-file project)
          sources (include-files project)
          arguments (if (empty? args) [] (vec args))
          params (apply concat sources arguments)]
      (with-json-file file content project
                          (generate-exclude-files project)
                          (invoke project params)))
    (catch Throwable t
      (error t (debug project))
      (main/abort))))
