import sbt._

object Webpack {
  lazy val devTask: TaskKey[Unit] = taskKey[Unit]("Webpack dev server")
  lazy val proTask: TaskKey[Unit] = taskKey[Unit]("Webpack assets for production")

  def runYarnInstall(dir: File): Int = {
    if ((dir / "node_modules").exists()) 0 else Process("yarn install", dir) !
  }

  def runDev(dir: File): Int = {
    val packagesInstall = runYarnInstall(dir)
    if (packagesInstall == 0) Process("yarn run dev", dir) ! else packagesInstall
  }

  def runBuild(dir: File): Int = {
    val packagesInstall = runYarnInstall(dir)
    if (packagesInstall == 0) Process("yarn run build", dir) ! else packagesInstall
  }
}