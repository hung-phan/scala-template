package infrastructure.persistence

import domain.model.Todo
import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.{ExecutionContext, Future}

class TodosRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                                implicit val ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import slick.jdbc.PostgresProfile.api._

  private val todos = TableQuery[TodosTable]

  def all(): Future[Seq[Todo]] = db.run(todos.result)

  def count(): Future[Int] = db.run(todos.length.result)

  def insert(todo: Todo): Future[Unit] = db.run(todos += todo).map { _ => () }

  def insert(todo: Seq[Todo]): Future[Unit] = db.run(todos ++= todo).map(_ => ())

  private class TodosTable(tag: Tag) extends Table[Todo](tag, "todos") {
    def * : ProvenShape[Todo] = (text, complete, id.?) <> (Todo.tupled, Todo.unapply)

    def text: Rep[String] = column[String]("text")

    def complete: Rep[Boolean] = column[Boolean]("complete")

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  }

}
