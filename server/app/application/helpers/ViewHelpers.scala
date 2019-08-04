package application.helpers

import io.circe._
import io.circe.parser._
import javax.inject.Inject
import play.twirl.api.Html

import scala.io.Source
import scala.util.Using

class ViewHelpers @Inject()(val env: play.api.Environment,
                            val config: play.api.Configuration) {
  val manifestString: String = Using(
    Source.fromFile(env.getFile("/manifest.json"))
  ) { reader => reader.getLines().mkString }.getOrElse("{}")

  val manifest: Either[ParsingFailure, Json] = parse(manifestString)

  def webpackManifest(): Html = {
    var htmlString = ""

    if (env.mode == play.api.Mode.Prod) {
      htmlString = s"<script>window.Manifest = $manifestString;</script>"
    }

    Html.apply(htmlString)
  }

  def webpackRuntime(): Html = {
    if (env.mode == play.api.Mode.Prod) {
      webpackJsAsset("runtime.js")
    } else {
      webpackJsAsset("main.app.js")
    }
  }

  def webpackJsAsset(name: String): Html = {
    var htmlString = ""

    if (env.mode == play.api.Mode.Prod) {
      manifest match {
        case Left(ex) => throw ex

        case Right(json) =>
          htmlString = s"<script type='text/javascript' src='${getAssetPath(json, name)}'></script>"
      }
    } else {
      htmlString = s"<script type='text/javascript' src='http://localhost:8080/$name'></script>"
    }

    Html.apply(htmlString)
  }

  def webpackCssAsset(name: String): Html = {
    var htmlString = ""

    if (env.mode == play.api.Mode.Prod) {
      manifest match {
        case Left(ex) => throw ex

        case Right(json) =>
          htmlString = s"<link rel='stylesheet' media='all' href='${getAssetPath(json, name)}' />"
      }
    }

    Html.apply(htmlString)
  }

  def getAssetPath(json: Json, name: String): String = json.hcursor.get[String](name).getOrElse("")
}
