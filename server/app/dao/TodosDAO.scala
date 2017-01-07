package dao

import javax.inject.Inject

import models.Todo
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.Future

class TodosDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import slick.driver.PostgresDriver.api._

  private class TodosTable(tag: Tag) extends Table[Todo](tag, "todos") {
    def text: Rep[String] = column[String]("text")

    def complete: Rep[Boolean] = column[Boolean]("complete")

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * : ProvenShape[Todo] = (text, complete, id.?) <> (Todo.tupled, Todo.unapply)
  }

  private val todos = TableQuery[TodosTable]

  def all(): Future[Seq[Todo]] = db.run(todos.result)

  def count(): Future[Int] = db.run(todos.length.result)

  def insert(todo: Todo): Future[Unit] = db.run(todos += todo).map { _ => () }

  def insert(todo: Seq[Todo]): Future[Unit] = db.run(todos ++= todo).map(_ => ())
}
