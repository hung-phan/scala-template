// Comment to get more information during initialization
logLevel := Level.Warn

// Resolvers
resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

resolvers += Resolver.url(
  "heroku-sbt-plugin-releases", url("https://dl.bintray.com/heroku/sbt-plugins/")
)(Resolver.ivyStylePatterns)

// Sbt plugins
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.13")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.3")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.6")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.21")

addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.6")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.2")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.4")

lazy val workbench = RootProject(uri("git://github.com/lihaoyi/workbench.git#75acf7d06cac7fe1798a585ee742005eac6d2ef9"))

lazy val root = (project in file(".")).dependsOn(workbench)
