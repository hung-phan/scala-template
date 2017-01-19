import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

object BuildEnvPlugin extends AutoPlugin {
  import autoImport._

  override def trigger = AllRequirements

  override def requires = JvmPlugin

  object autoImport {

    object BuildEnv extends Enumeration {
      val Production, Testing, Development = Value
    }

    val buildEnv: SettingKey[BuildEnv.Value] = settingKey[BuildEnv.Value]("the current build environment")
  }

  override def projectSettings: Seq[Setting[_]] = Seq(
    buildEnv := {
      sys.props.get("env")
        .orElse(sys.env.get("BUILD_ENV"))
        .flatMap {
          case "Production" => Some(BuildEnv.Production)
          case "Testing" => Some(BuildEnv.Testing)
          case _ => Some(BuildEnv.Development)
        }
        .getOrElse(BuildEnv.Development)
    },

    onLoadMessage := {
      val defaultMessage = onLoadMessage.value
      val env = buildEnv.value
      s"""|$defaultMessage
          |Running in build environment: $env""".stripMargin
    }
  )
}
