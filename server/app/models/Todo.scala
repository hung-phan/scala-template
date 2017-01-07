package models

case class Todo(text: String, complete: Boolean, id: Option[Long] = None)
