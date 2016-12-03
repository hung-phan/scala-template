import scala.language.postfixOps

lazy val scalaV = "2.11.8"

lazy val webpackBuild = taskKey[Unit]("Webpack build for the application")

def runYarnInstall(dir: File) = {
  println(dir / "node_modules")
  if ((dir / "node_modules").exists()) 0 else Process("yarn install", dir) !
}

def runBuild(dir: File) = {
  val packagesInstall = runYarnInstall(dir)
  if (packagesInstall == 0) Process("yarn run build", dir) ! else packagesInstall
}

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(scalaVersion := scalaV)
  .jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val client = (project in file("client"))
  .settings(workbenchSettings: _*)
  .settings(
    bootSnippet := "example.ScalaJSExample().main();",
    updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)
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

lazy val ui = project in file("ui")

lazy val server = (project in file("server"))
  .settings(
    scalaVersion := scalaV,
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    // webpack assets
    webpackBuild := {
      if (runBuild(ui.base) != 0) throw new Exception("Oops! UI Build crashed.")
    },
    unmanagedResourceDirectories in Assets += (ui.base / "build"),
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile <<= (compile in Compile).dependsOn(scalaJSPipeline, webpackBuild),
    libraryDependencies ++= Seq(
      "com.h2database" % "h2" % "1.4.193",
      "com.typesafe.play" %% "play-slick" % "2.0.0",
      "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
      "com.vmunier" %% "scalajs-scripts" % "1.0.0",
      specs2 % Test
    ),
    // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
    EclipseKeys.preTasks := Seq(compile in Compile)
  )
  .enablePlugins(PlayScala)
  .dependsOn(sharedJvm)

// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
