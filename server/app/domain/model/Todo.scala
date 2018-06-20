package domain.model

case class Todo(text: String, complete: Boolean, id: Option[Long] = None)
