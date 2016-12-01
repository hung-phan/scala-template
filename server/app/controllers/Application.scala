package controllers

import javax.inject.Inject

import helpers.ViewHelpers
import play.api.mvc._

class Application @Inject()(implicit val viewHelpers: ViewHelpers) extends Controller {
  def index = Action {
    Ok(views.html.index())
  }
}
