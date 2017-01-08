package bootstrap

import javax.inject.Inject

import dao.TodosDAO
import models.Todo
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

class InitialData @Inject()(todosDAO: TodosDAO) {
  // run on start

  Try(Await.result(
    todosDAO.count().map((size: Int) => {
      if (size == 0) {
        todosDAO.insert(
          for (index <- 1 to 10)
            yield Todo(s"Todo $index", complete = false)
        )
      }
    }),
    Duration.Inf
  ))
}
