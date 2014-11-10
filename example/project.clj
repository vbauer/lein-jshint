(defproject example "0.1.0-SNAPSHOT"
  :description "Simple example of using lein-jshint"
  :url "https://github.com/vbauer/lein-jshint"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}


  ; List of plugins
  :plugins [[lein-jshint "0.1.8"]]

  ; List of hooks
  ; It's used for running JSHint during compile phase
  :hooks [lein-jshint.plugin]

  ; JSHint configuration
  :jshint {:debug true
           :includes "resources/*.js"})
