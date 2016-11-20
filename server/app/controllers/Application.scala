package controllers

import javax.inject.Inject

import dao.TodosDAO
import helpers.ViewHelpers
import play.api.mvc._

class Application @Inject()(todosDAO: TodosDAO, implicit val viewHelpers: ViewHelpers) extends Controller {
  def index = Action {
    Ok(views.html.index())
  }
}
