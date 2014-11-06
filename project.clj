(defproject lein-jshint "0.1.6-SNAPSHOT"
  :description "A Leiningen plugin for running JS code through JSHint."
  :url "https://github.com/vbauer/lein-jshint"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[clj-glob "1.0.0" :exclusions [org.clojure/clojure]]
                 [lein-npm "0.4.0" :exclusions [org.clojure/clojure]]]

  :plugins [[jonase/eastwood "0.1.5" :exclusions [org.clojure/clojure]]
            [lein-release "1.0.6" :exclusions [org.clojure/clojure]]
            [lein-kibit "0.0.8" :exclusions [org.clojure/clojure]]
            [lein-bikeshed "0.1.8" :exclusions [org.clojure/clojure]]
            [lein-ancient "0.5.5"]]

  :pedantic? :abort
  :eval-in-leiningen true

  :local-repo-classpath true
  :lein-release {:deploy-via :clojars
                 :scm :git})
