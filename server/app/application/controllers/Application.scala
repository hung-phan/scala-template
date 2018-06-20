package application.controllers

import application.helpers.ViewHelpers
import javax.inject.Inject
import play.api.libs.ws.WSClient
import play.api.mvc._
import shared.SharedMessages

import scala.concurrent.ExecutionContext

class Application @Inject()(
                             controllerComponents: ControllerComponents,
                             val config: play.api.Configuration,
                             val ws: WSClient,
                             implicit val viewHelpers: ViewHelpers,
                             implicit val ec: ExecutionContext
                           ) extends AbstractController(controllerComponents) {
  def index: Action[AnyContent] = Action {
    Ok(application.views.html.index(SharedMessages.itWorks))
  }
}
