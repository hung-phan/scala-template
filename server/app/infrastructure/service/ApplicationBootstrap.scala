package infrastructure.service

import com.google.inject.AbstractModule

class ApplicationBootstrap extends AbstractModule {
  override protected def configure(): Unit = {
    bind(classOf[InitialData]).asEagerSingleton()
  }
}
