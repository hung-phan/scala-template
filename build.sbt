import com.typesafe.sbt.packager.MappingsHelper._

lazy val scalaV = "2.12.4"
lazy val circeVersion = "0.9.3"

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(scalaVersion := scalaV)
  .jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val client = (project in file("client"))
  .settings(
    scalaVersion := scalaV,
    scalaJSUseMainModuleInitializer := true,
    scalaJSUseMainModuleInitializer in Test := false,
    libraryDependencies ++= Seq(
      guice,
      "org.scala-js" %%% "scalajs-dom" % "0.9.4"
    )
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb, WorkbenchSplicePlugin)
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
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser",
      "io.circe" %% "circe-optics"
    ).map(_ % circeVersion),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "com.typesafe.play" %% "play-slick" % "3.0.3",
      "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3",
      "org.postgresql" % "postgresql" % "42.2.2",
      "com.vmunier" %% "scalajs-scripts" % "1.1.2",
      specs2 % Test
    ),
    // docker base
    dockerBaseImage := "openjdk"
  )
  .enablePlugins(PlayScala)
  .dependsOn(sharedJvm)

// loads the server project at sbt startup
onLoad in Global ~= (_ andThen ("project server" :: _))
