// Comment to get more information during initialization
logLevel := Level.Warn

// Resolvers
resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

resolvers += Resolver.url(
  "heroku-sbt-plugin-releases", url("https://dl.bintray.com/heroku/sbt-plugins/")
)(Resolver.ivyStylePatterns)

// Sbt plugins
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.20")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.8")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.25")

addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.8-0.6")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.2")

addSbtPlugin("com.lihaoyi" % "workbench" % "0.4.1")

//lazy val workbench = RootProject(uri("git://github.com/lihaoyi/workbench.git#75acf7d06cac7fe1798a585ee742005eac6d2ef9"))
//
//lazy val root = (project in file(".")).dependsOn(workbench)
