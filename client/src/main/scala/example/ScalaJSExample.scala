package example

import org.scalajs.dom
import shared.SharedMessages

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("ScalaJSExample")
object ScalaJSExample {
  @JSExport
  def main(): Unit = {
    dom.document.getElementById("app").textContent = s"Client data: ${SharedMessages.itWorks}"
  }
}
