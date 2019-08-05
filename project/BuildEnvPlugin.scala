import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

object BuildEnvPlugin extends AutoPlugin {

  override def trigger = AllRequirements

  override def requires = JvmPlugin

  object autoImport {

    object BuildEnv extends Enumeration {
      val Production, Testing, Development = Value
    }

    val buildEnv = settingKey[BuildEnv.Value]("the current build environment")
  }
  import autoImport._

  override def projectSettings: Seq[Setting[_]] = Seq(
    buildEnv := {
      sys.props.get("env")
        .orElse(sys.env.get("BUILD_ENV"))
        .flatMap {
          case "prod" => Some(BuildEnv.Production)
          case "test" => Some(BuildEnv.Testing)
          case _ => Some(BuildEnv.Development)
        }
        .getOrElse(BuildEnv.Development)
    }
  )
}
