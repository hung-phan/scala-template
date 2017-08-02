package infrastructure.bootstrap

import javax.inject.Inject

import domain.models.Todo
import domain.repositories.TodosRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.util.Try

class InitialData @Inject()(todosRepository: TodosRepository,
                            implicit val ec: ExecutionContext) {
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
