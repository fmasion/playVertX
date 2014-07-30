package playvertx

import org.vertx.java.platform.PlatformManager
import org.vertx.java.platform.PlatformLocator
import playHazelcast.api.PlayHz

object VertX {

  private var plateformManager: Option[PlatformManager] = None

  private def start = {
    val maybeClustered = hz.map(_.getCluster.getLocalMember.getSocketAddress.getAddress.getHostAddress)
    plateformManager = Some(maybeClustered.fold(PlatformLocator.factory.createPlatformManager())(host => PlatformLocator.factory.createPlatformManager(0, host)))
  }

  private[playvertx] def stop = {
    plateformManager.map(_.stop())
    plateformManager = None
  }

  private[playvertx] def restart = {
    stop
    start
  }

  def pm = plateformManager

  def vertx = plateformManager.map(_.vertx())

  def hz = if (VertxConf.isClustered) {
    PlayHz.getInstance
  } else None

}
