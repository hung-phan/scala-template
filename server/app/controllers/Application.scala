package controllers

import com.google.inject.Inject
import play.api.mvc._
import play.api.{Configuration, Environment}
import shared.SharedMessages

class Application @Inject()(environment: Environment, configuration: Configuration) extends Controller {
  def index = Action {
    Ok(views.html.index(environment, configuration)(SharedMessages.itWorks))
  }
}
