# Play Framework with Scala.js

[![License](http://img.shields.io/:license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

This is a simple example application showing how you can integrate a Play project with a Scala.js project.

The application contains three directories:
* `server` Play application (server side).
* `client` Scala.js application (client side).
* `shared` Scala code that you want to share between the server and the client.

## Run the application

### Requirements

- [node](https://nodejs.org/en/)
- [yarn](https://yarnpkg.com/)

### Development
```bash
$ docker-compose up -d
$ sbt -Dconfig.file=server/conf/development.conf run
$ cd server/js-frontend && yarn run dev # in separated terminal
```

### Production

Run this to build all the assets using webpack bundler. You must see the profile to know how to
replace application configuration with your environment configuration.

```bash
$ sbt -Denv=Production webpackProTask
$ sbt -Denv=Production docker:publishLocal # Replace docker:publishLocal with other built tasks on your demand
```

To start it in local, run:

```bash
$ docker-compose -f docker-compose.prod.yml up
```

## Features

The application uses the [sbt-web-scalajs](https://github.com/vmunier/sbt-web-scalajs) sbt plugin and
the [scalajs-scripts](https://github.com/vmunier/scalajs-scripts) library.

- Run your application like a regular Play app
  - `compile` triggers the Scala.js fastOptJS command.
  - `run` triggers the Scala.js fastOptJS command on page refresh.
  - `~compile`, `~run`, continuous compilation is also available.
- Compilation errors from the Scala.js projects are also displayed in the browser.
- Production archives (e.g. using `stage`, `dist`) contain the optimised javascript.
- Source maps
  - Open your browser dev tool to set breakpoints or to see the guilty line of code when an exception is thrown.
  - Source Maps is _disabled in production_ by default to prevent your users from seeing the source files. But it can
  easily be enabled in production too by setting `emitSourceMaps in fullOptJS := true` in the Scala.js projects.

## Cleaning

The root project aggregates all the other projects by default.
Use this root project, called `scala-template`, to clean all the projects at once.

```bash
$ sbt clean
$ rm -rf server/js-frontend/build
```
