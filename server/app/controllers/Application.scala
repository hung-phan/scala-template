package controllers

import javax.inject.Inject

import dao.TodosDAO
import helpers.ViewHelpers
import models.Todo
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

class Application @Inject()(todosDAO: TodosDAO, implicit val viewHelpers: ViewHelpers) extends Controller {
  def index: Action[AnyContent] = Action.async {
    todosDAO.all().map((todos: Seq[Todo]) => {
      println(todos)
      Ok(views.html.index())
    })
  }
}
