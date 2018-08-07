# Play Framework with Scala.js

[![build status](https://secure.travis-ci.org/hung-phan/scala-template.svg)](http://travis-ci.org/hung-phan/scala-template/)

This is a simple example application showing how you can integrate a Play project with a Scala.js project.

The application contains three directories:
* `server` Play application (server side).
* `client` Scala.js application (client side).
* `shared` Scala code that you want to share between the server and the client.

## Run the application

### Requirements

- [node](https://nodejs.org/en/)
- [yarn](https://yarnpkg.com/)

Install npm dependencies

```bash
$ cd server/frontend && yarn # run this only once
```

### Development
```bash
$ docker-compose up -d
$ sbt -Dconfig.file=server/conf/development.conf run
$ cd server/frontend && npm run start # in separated terminal
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
$ ./scripts/clean.sh
```
