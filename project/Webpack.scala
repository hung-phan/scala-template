import sbt._
import scala.sys.process._

object Webpack {
  lazy val webpackDevTask: TaskKey[Unit] = taskKey[Unit]("Webpack dev server")
  lazy val webpackProTask: TaskKey[Unit] = taskKey[Unit]("Webpack assets for production")

  def runNpmInstall(dir: File): Int = {
    Process("npm install", dir) !
  }

  def runDev(dir: File): Int = {
    val packagesInstall = runNpmInstall(dir)
    if (packagesInstall == 0) Process("npm run start", dir) ! else packagesInstall
  }

  def runBuild(dir: File): Int = {
    val packagesInstall = runNpmInstall(dir)
    if (packagesInstall == 0) Process("npm run build", dir) ! else packagesInstall
  }
}
