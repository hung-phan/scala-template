package infrastructure.bootstrap

import javax.inject.Inject

import domain.models.Todo
import domain.repositories.TodosRepository
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

class InitialData @Inject()(todosRepository: TodosRepository) {
  Try(Await.result(
    todosRepository.count().map((size: Int) => {
      if (size == 0) {
        todosRepository.insert(
          for (index <- 1 to 10)
            yield Todo(s"Todo $index", complete = false)
        )
      }
    }),
    Duration.Inf
  ))
}
