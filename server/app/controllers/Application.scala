package controllers

import com.google.inject.Inject
import play.api.Environment
import play.api.mvc._
import shared.SharedMessages

class Application @Inject()(environment: Environment) extends Controller {
  def index = Action {
    Ok(views.html.index(environment)(SharedMessages.itWorks))
  }
}
