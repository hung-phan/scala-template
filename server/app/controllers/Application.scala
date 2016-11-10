package controllers

import javax.inject.Inject

import helpers.ViewHelpers
import play.api.mvc._
import shared.SharedMessages

class Application @Inject()(implicit viewHelpers: ViewHelpers) extends Controller {
  def index = Action {
    Ok(views.html.index(SharedMessages.itWorks))
  }
}
