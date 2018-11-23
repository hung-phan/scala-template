package example

import org.scalajs.dom
import shared.SharedMessages

import scala.scalajs.js.annotation.JSExportTopLevel

object ScalaJSExample {
  @JSExportTopLevel("example.ScalaJSExample")
  def main(): Unit = {
    dom.document.getElementById("app").textContent = s"Client data: ${SharedMessages.itWorks}"
  }
}
