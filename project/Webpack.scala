import java.net.InetSocketAddress

import play.sbt.PlayRunHook
import sbt._

object Webpack {
  def apply(base: File): PlayRunHook = {
    object WebpackProcess extends PlayRunHook {
      var process: Option[Process] = None

      override def beforeStarted(): Unit = {
        if (!(base / "node_modules").exists()) {
          Process("npm install", base).run
        }
      }

      override def afterStarted(addr: InetSocketAddress): Unit = {
        process = Some(Process("npm run dev", base).run)
      }

      override def afterStopped(): Unit = {
        process.foreach(_.destroy())
        process = None
      }
    }

    WebpackProcess
  }
}