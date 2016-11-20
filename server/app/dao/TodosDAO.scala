package dao

import javax.inject.Inject

import models.Todo
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

class TodosDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  private class TodosTable(tag: Tag) extends Table[Todo](tag, "Todos") {
    def id = column[Long]("id", O.PrimaryKey)

    def text = column[String]("text")

    def complete = column[Boolean]("complete")

    def * = (id, text, complete) <> (Todo.tupled, Todo.unapply _)
  }

  private val todos = TableQuery[TodosTable]

  def all(): Future[Seq[Todo]] = db.run(todos.result)

  def count(): Future[Int] = db.run(todos.length.result)

  def insert(todo: Todo): Future[Unit] = db.run(todos += todo).map { _ => () }

  def insert(todo: Seq[Todo]): Future[Unit] = db.run(todos ++= todo).map(_ => ())
}
