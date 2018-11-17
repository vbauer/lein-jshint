lein-jshint
===========

[JSHint](https://github.com/jshint/jshint) is a community-driven tool to detect errors and potential problems in JavaScript code. It is very flexible so you can easily adjust it to your particular coding guidelines and the environment you expect your code to execute in.

[lein-jshint](https://github.com/vbauer/lein-jshint) is a Leiningen plugin that allows to do static analysis for JavaScript files using JSHint.

[![Build Status](https://travis-ci.org/vbauer/lein-jshint.svg?branch=master)](https://travis-ci.org/vbauer/lein-jshint)
[![Clojars Project](https://img.shields.io/clojars/v/lein-jshint.svg)](https://clojars.org/lein-jshint)


Pre-requirements
================

Install [NodeJS](http://nodejs.org/) and [NPM](https://github.com/npm/npm) (package manager for Node) to install JSHint:

- On Ubuntu: `sudo apt-get install nodejs`
- On Mac OS X: `brew install node`


Installation
============

Install [JSHint](https://www.npmjs.org/package/jshint) to use lein-jshint plugin. It could be done in few ways:

- Use NPM to install JSHint globally: `npm install jshint -g`
- You can also install JSHint in the current directory: `npm install jshint`
- Use [lein-npm](https://github.com/bodil/lein-npm) plugin: `lein npm install`
- Use just Leiningen: `lein deps`

Setup
-----

To enable lein-jshint for your project, put the following in the :plugins vector of your project.clj file:

```clojure
; Use latest version instead of "X.X.X"
:plugins [[lein-jshint "X.X.X"]]
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
         :eqeqeq true
         ...}
```

You can use both variants to specify keys: string values or keywords.

All available parameters are described in the official documentation here: http://www.jshint.com/docs/options/


Hooks
-----

To enable this plugin in compile stage, use the following hook:
```clojure
:hooks [lein-jshint.plugin]
```


Examples
========

Detailed example
----------------

```clojure
:jshint {
  :includes ["resources/public/js/*.js"]
  :excludes ["resources/public/js/directives.js"]

  ; This configuration is used by default
  :config {:bitwise    true    ; Prohibit bitwise operators (&, |, ^, etc.)
           :curly      true    ; Require {} for every new block or scope
           :eqeqeq     true    ; Require triple equals i.e. ===
           :forin      true    ; Tolerate "for in" loops without hasOwnPrototype
           :latedef    true    ; Prohibit variable use before definition
           :noarg      true    ; Prohibit use of arguments.caller and arguments.callee
           :nonew      true    ; Prohibit use of constructors for side-effects
           :plusplus   true    ; Prohibit use of "++" & "--"
           :undef      true    ; Require all non-global vars be declared before usage
           :strict     true    ; Require "use strict" pragma in every file
           }}
```
Just for Code Maniacs: [JSHint Configuration, Strict Edition](https://gist.github.com/haschek/2595796)


Example project
---------------

Just clone the current repository and try to play with [example project](https://github.com/vbauer/lein-jshint/tree/master/example) for better understanding how to use lein-jshint.


Invoking JSHint
===============

It is also possible to invoke JSHint directly using "jshint" task:
```
lein jshint "resources/js/*.js"
lein jshint -verbose
lein jshint --reporter=checkstyle resources/public/js/controllers.js
```
See all CLI commands here: http://www.jshint.com/docs/cli/


Unit testing
============

To run unit tests:

```bash
lein test
```


Thanks to
=========

JSHint author [Anton Kovalyov](http://anton.kovalyov.net) and other developers who worked on this [great project](https://github.com/jshint/jshint/graphs/contributors).


Might also like
===============

* [lein-asciidoctor](https://github.com/asciidoctor/asciidoctor-lein-plugin) - A Leiningen plugin for generating documentation using Asciidoctor.
* [lein-plantuml](https://github.com/vbauer/lein-plantuml) - a Leiningen plugin for generating UML diagrams using PlantUML.
* [lein-coffeescript](https://github.com/vbauer/lein-coffeescript) - a Leiningen plugin for running CoffeeScript compiler.
* [lein-typescript](https://github.com/vbauer/lein-typescript) - a Leiningen plugin for running TypeScript compiler.
* [lein-jslint](https://github.com/vbauer/lein-jslint) - a Leiningen plugin for running javascript code through JSLint.
* [jabberjay](https://github.com/vbauer/jabberjay) - a simple framework for creating Jabber bots.
* [coderwall-clj](https://github.com/vbauer/coderwall-clj) - a tiny CoderWall client for Clojure.


License
=======

Copyright © 2014 Vladislav Bauer

Distributed under the Eclipse Public License, the same as Clojure.
