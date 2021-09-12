// Comment to get more information during initialization
logLevel := Level.Warn

// Resolvers

resolvers += "spray repo" at "https://repo.spray.io"

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

resolvers += Resolver.url(
  "heroku-sbt-plugin-releases", url("https://dl.bintray.com/heroku/sbt-plugins/")
)(Resolver.ivyStylePatterns)

// Sbt plugins
addSbtPlugin("com.vmunier"        % "sbt-web-scalajs"          % "1.1.0")
addSbtPlugin("org.scala-js"       % "sbt-scalajs"              % "1.1.1")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")
addSbtPlugin("com.typesafe.play"  % "sbt-plugin"               % "2.8.8")
addSbtPlugin("com.timushev.sbt"   % "sbt-updates"              % "0.5.0")
addSbtPlugin("com.eed3si9n"       % "sbt-assembly"             % "0.14.10")
addSbtPlugin("com.typesafe.sbt"   % "sbt-gzip"                 % "1.0.2")

//lazy val workbench = RootProject(uri("git://github.com/lihaoyi/workbench.git#75acf7d06cac7fe1798a585ee742005eac6d2ef9"))
//
//lazy val root = (project in file(".")).dependsOn(workbench)
