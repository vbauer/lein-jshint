lein-jshint
===========

[JSHint](https://github.com/jshint/jshint) is a community-driven tool to detect errors and potential problems in JavaScript code. It is very flexible so you can easily adjust it to your particular coding guidelines and the environment you expect your code to execute in.

[lein-jshint](https://github.com/vbauer/lein-jshint) is a Leiningen plugin that allows to do static analysis for JavaScript files using JSHint.


Pre-requirements
================

Install [NodeJS](http://nodejs.org/) and [NPM](https://github.com/npm/npm) (package manager for Node) to install JSHint:

On Ubuntu:
```
sudo apt-get install nodejs
```
On Mac OS X:
```
brew install node
```


Installation
============

To enable lein-jshint for your project, put the following in the :plugins vector of your project.clj file:

![latest-version](https://clojars.org/lein-jshint/latest-version.svg)

[![Build Status](https://travis-ci.org/vbauer/lein-jshint.svg?branch=master)](https://travis-ci.org/vbauer/lein-jshint)
[![Dependencies Status](http://jarkeeper.com/vbauer/lein-jshint/status.png)](http://jarkeeper.com/vbauer/lein-jshint)

Install [JSHint](https://www.npmjs.org/package/jshint) to use lein-jshint plugin. It could be done in few ways:

- Use NPM to install JSHint globally:
```
npm install jshint -g
```
- You can also install JSHint in the current directory:
```
npm install jslint
```
- Use [lein-npm](https://github.com/bodil/lein-npm) plugin:
```
lein npm install
```
- Use just Leiningen:
```
lein deps
```

To enable this plugin in compile stage, use the following hook:
```clojure
:hooks [lein-jshint.plugin]
```


Configuration
=============

lien-jshint will create two files in runtime to setup configuration:
- [.jshintrc](https://github.com/jshint/jshint/blob/2.x/examples/.jshintrc) - main JSHint configuration
- [.jshintignore](https://github.com/jshint/jshint/blob/2.x/examples/.jshintignore) - list of files for ignoring

You can specify place, where JS files will be located with:
```clojure
:jshint {
  :includes ["resources/public/js/*.js"
             "resources/js/*.js"]
}
```

You can also specify JS files that should be excluded from checking:
```clojure
:jshint { :excludes ["resources/public/lib/*.js"] }
```

To specify *:includes* and *:excludes* options, it is possible to use <a href="http://en.wikipedia.org/wiki/Glob_(programming)">Glob Patterns</a>.

JSHint rules could be configured with *:config* parameter:
```clojure
; It specifies which JSHint options to turn on or off
:config {:globals {:angular true
                   :console true
                   "$" true}
         :node true
         :es5 true
         :eqeqeq true
         ...}
```

You can use both variants to specify keys: string values or keywords.

All available parameters are described in the official documentation here: http://www.jshint.com/docs/options/

Configuration example:
```clojure
:jshint {
  :includes ["resources/public/js/*.js"]
  :excludes ["resources/public/js/directives.js"]

  ; This configuration is used by default
  :config {:bitwise    true    ; Prohibit bitwise operators (&, |, ^, etc.)
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
           }}
```
Just for Code Maniacs: [JSHint Configuration, Strict Edition](https://gist.github.com/haschek/2595796)


Invoking JSHint
===============

It is also possible to invoke JSHint directly using "jshint" task:
```
lein jshint "resources/js/*.js"
lein jshint -verbose
lein jshint --reporter=checkstyle resources/public/js/controllers.js
```
See all CLI commands here: http://www.jshint.com/docs/cli/


License
=======

Copyright © 2014 Vladislav Bauer

Distributed under the Eclipse Public License, the same as Clojure.


Might also like
===============

* [lein-asciidoc](https://github.com/vbauer/lein-asciidoc) - A Leiningen plugin for generating documentation using Asciidoctor.
* [lein-jslint](https://github.com/vbauer/lein-jslint) - a Leiningen plugin for running javascript code through JSLint.
* [lein-plantuml](https://github.com/vbauer/lein-plantuml) - a Leiningen plugin for generating UML diagrams using PluntUML.
