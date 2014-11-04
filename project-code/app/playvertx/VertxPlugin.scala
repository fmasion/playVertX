package playvertx

import play.api._

class VertxPlugin(app:Application) extends Plugin {
  val log = Logger("playvertx.VertxPlugin")

  override def onStart() {
    log.info(s"Starting vert.x plugin (${BuildInfo.version}, embedding vert.x : ${BuildInfo.vertxVersion})")
    VertX.restart()
  }

  override def onStop() {
    VertX.stop()
    log.info("Stoped vert.x")
  }

}
