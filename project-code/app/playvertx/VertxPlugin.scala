package playvertx

import play.api._
import org.vertx.java.platform.PlatformLocator
import org.vertx.java.core.VertxFactory

class VertxPlugin(app:Application) extends Plugin {
  val log = Logger("VertxPlugin")

  override def onStart() {
    log.info("Starting vert.x")
    VertX.restart
  }

  override def onStop() {
    VertX.stop
    log.info("Stoped vert.x")
  }

}
