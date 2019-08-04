import org.junit.runner._
import org.specs2.runner._
import play.api.mvc
import play.api.test._

import scala.concurrent.Future

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec() extends PlaySpecification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication {
      val boom: Future[mvc.Result] = route(app, FakeRequest(GET, "/boom")).get

      status(boom) mustEqual NOT_FOUND
    }

    "render the index page" in new WithApplication {
      val home: Future[mvc.Result] = route(app, FakeRequest(GET, "/")).get

      status(home) mustEqual OK
      contentAsString(home) must contain("Server data: It works")
    }
  }
}
