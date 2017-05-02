package application.controllers

import javax.inject.Inject

import application.helpers.ViewHelpers
import play.api.mvc._
import shared.SharedMessages

class Application @Inject()(implicit val viewHelpers: ViewHelpers) extends Controller {
  def index: Action[AnyContent] = Action {
    Ok(application.views.html.index(SharedMessages.itWorks))
  }
}
