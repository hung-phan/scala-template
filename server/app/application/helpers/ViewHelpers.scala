package application.helpers

import io.circe._
import io.circe.parser._
import javax.inject.Inject
import play.twirl.api.Html

import scala.io.Source
import scala.util.Try

class ViewHelpers @Inject()(val env: play.api.Environment,
                            val config: play.api.Configuration) {
  val manifest: Either[ParsingFailure, Json] = parse(
    Try(Source.fromFile(env.getFile("/manifest.json")).getLines.mkString).getOrElse("{}")
  )

  def webpackJsAsset(name: String): Html = {
    if (env.mode == play.api.Mode.Prod)
      manifest match {
        case Left(ex) => throw ex

        case Right(json) =>
          Html.apply(s"<script type='text/javascript' src='/assets${getAssetPath(json, name)}'></script>")
      }
    else Html.apply(s"<script type='text/javascript' src='http://localhost:8080/$name'></script>")
  }

  def webpackCssAsset(name: String): Html = {
    if (env.mode == play.api.Mode.Prod)
      manifest match {
        case Left(ex) => throw ex

        case Right(json) =>
          Html.apply(s"<link rel='stylesheet' media='all' href='/assets${getAssetPath(json, name)}' />")
      }
    else Html.apply("")
  }

  def getAssetPath(json: Json, name: String): String = json.hcursor.get[String](name).getOrElse("")
}
