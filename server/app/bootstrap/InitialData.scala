package bootstrap

import javax.inject.Inject

import dao.TodosDAO
import models.Todo

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

class InitialData @Inject()(todosDAO: TodosDAO) {
  Try(Await.result(
    todosDAO.insert(Seq(
      Todo(1L, "Hello", complete = true),
      Todo(2L, "Nghi", complete = true)
    )),
    Duration.Inf
  ))
}
