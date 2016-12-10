package helpers

import javax.inject.Inject

import play.api.libs.json.{JsString, JsValue, Json}
import play.twirl.api.Html

import scala.io.Source
import scala.util.{Failure, Success, Try}

class ViewHelpers @Inject()(val env: play.api.Environment,
                            val config: play.api.Configuration) {
  val manifest: Try[JsValue] = Try(Json.parse(
    Source.fromFile("server/js-frontend/build/manifest.json").getLines.mkString
  ))

  def webpackJsAsset(name: String): Html = {
    if (env.mode == play.api.Mode.Dev)
      manifest match {
        case Success(json) =>
          Html.apply(s"<script type='text/javascript' src='/assets/${(json \ name).as[JsString].value}'></script>")

        case Failure(ex) => throw ex
      }
    else Html.apply(s"<script type='text/javascript' src='http://localhost:8080/build/$name'></script>")
  }

  def webpackCssAsset(name: String): Html = {
    if (env.mode == play.api.Mode.Dev)
      manifest match {
        case Success(json) =>
          Html.apply(s"<link rel='stylesheet' media='all' href='/assets/${(json \ name).as[JsString].value}' />")

        case Failure(ex) => throw ex
      }
    else Html.apply("")
  }
}
