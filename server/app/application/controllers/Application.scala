package application.controllers

import javax.inject.Inject

import application.helpers.ViewHelpers
import play.api.mvc._
import shared.SharedMessages

class Application @Inject() (
  controllerComponents: ControllerComponents,
  implicit val viewHelpers: ViewHelpers
) extends AbstractController(controllerComponents) {
  def index: Action[AnyContent] = Action {
    Ok(application.views.html.index(SharedMessages.itWorks))
  }
}
