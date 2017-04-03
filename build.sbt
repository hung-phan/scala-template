import com.typesafe.sbt.packager.MappingsHelper._

lazy val scalaV = "2.12.1"

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(scalaVersion := scalaV)
  .jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val client = (project in file("client"))
  .settings(workbenchSettings: _*)
  .settings(
    bootSnippet := "example.ScalaJSExample().main();",
    updateBrowsers := (updateBrowsers triggeredBy (fastOptJS in Compile)).value
  )
  .settings(
    scalaVersion := scalaV,
    persistLauncher := true,
    persistLauncher in Test := false,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1"
    )
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(sharedJs)

lazy val webpackBuild = taskKey[Unit]("Webpack build for the application")

lazy val server = (project in file("server"))
  .settings(
    scalaVersion := scalaV,
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(gzip),
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    Webpack.webpackDevTask := {
      Webpack.runDev(baseDirectory.value / "js-frontend")
    },
    Webpack.webpackProTask := {
      Webpack.runBuild(baseDirectory.value / "js-frontend")
    },
    dist := (dist dependsOn Webpack.webpackProTask).value,
    stage := (stage dependsOn Webpack.webpackProTask).value,
    unmanagedResourceDirectories in Assets += (baseDirectory.value / "js-frontend" / "build"),
    mappings in Universal ++= directory(baseDirectory.value / "js-frontend" / "build" / "manifest.json"),
    mappings in Universal += {
      val confFile = buildEnv.value match {
        case BuildEnv.Production => "production.conf"
        case BuildEnv.Testing => "testing.conf"
        case _ => "development.conf"
      }
      ((resourceDirectory in Compile).value / confFile) -> "conf/application.conf"
    },
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-slick" % "2.1.0",
      "com.typesafe.play" %% "play-slick-evolutions" % "2.1.0",
      "org.postgresql" % "postgresql" % "42.0.0",
      "com.vmunier" %% "scalajs-scripts" % "1.1.0",
      specs2 % Test
    ),
    // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
    EclipseKeys.preTasks := Seq(compile in Compile),
    // docker base
    dockerBaseImage := "openjdk"
  )
  .enablePlugins(PlayScala)
  .dependsOn(sharedJvm)

// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
