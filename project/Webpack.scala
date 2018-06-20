import sbt._
import scala.sys.process._

object Webpack {
  lazy val webpackDevTask: TaskKey[Unit] = taskKey[Unit]("Webpack dev server")
  lazy val webpackProTask: TaskKey[Unit] = taskKey[Unit]("Webpack assets for production")

  def runYarnInstall(dir: File): Int = {
    val yarnCommandStatus: Int = Process("yarn", dir) !

    if (yarnCommandStatus != 0) {
      Process("npm install -g yarn && yarn", dir)
    }

    yarnCommandStatus
  }

  def runDev(dir: File): Int = {
    val packagesInstall = runYarnInstall(dir)
    if (packagesInstall == 0) Process("npm run start", dir) ! else packagesInstall
  }

  def runBuild(dir: File): Int = {
    val packagesInstall = runYarnInstall(dir)
    if (packagesInstall == 0) Process("npm run build", dir) ! else packagesInstall
  }
}