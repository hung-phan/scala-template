import com.typesafe.sbt.packager.MappingsHelper._
import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

lazy val circeVersion = "0.13.0"
lazy val playSlickVersion = "5.0.0"

lazy val commonSettings = Seq(
  scalaVersion := "2.13.1",
)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(commonSettings)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val client = (project in file("client"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      guice,
      "org.scala-js" %%% "scalajs-dom" % "0.9.7"
    )
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(sharedJs)

lazy val webpackBuild = taskKey[Unit]("Webpack build for the application")

lazy val server = (project in file("server"))
  .settings(commonSettings)
  .settings(
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(gzip),
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    Webpack.webpackDevTask := {
      Webpack.runDev(baseDirectory.value / "frontend")
    },
    Webpack.webpackProTask := {
      Webpack.runBuild(baseDirectory.value / "frontend")
    },
    dist := (dist dependsOn Webpack.webpackProTask).value,
    stage := (stage dependsOn Webpack.webpackProTask).value,
    unmanagedResourceDirectories in Assets += (baseDirectory.value / "frontend" / "dist"),
    mappings in Universal ++= directory(baseDirectory.value / "frontend" / "dist" / "manifest.json"),
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
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "com.typesafe.play" %% "play-slick" % playSlickVersion,
      "com.typesafe.play" %% "play-slick-evolutions" % playSlickVersion,
      "org.postgresql" % "postgresql" % "42.2.14",
      "com.vmunier" %% "scalajs-scripts" % "1.1.4",
      specs2 % Test
    ),
    // docker base
    dockerBaseImage := "openjdk:12"
  )
  .enablePlugins(PlayScala)
  .dependsOn(sharedJvm, client)

// loads the server project at sbt startup
onLoad in Global ~= (_ andThen ("project server" :: _))
