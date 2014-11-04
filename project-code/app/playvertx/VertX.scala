package playvertx

import com.hazelcast.core.HazelcastInstance
import org.vertx.java.core.Vertx
import org.vertx.java.platform.PlatformManager
import org.vertx.java.platform.PlatformLocator
import playHazelcast.api.PlayHz

object VertX {

  private var plateformManager: Option[PlatformManager] = None

  private def start() = {
    val maybeClustered = hz.map(_.getCluster.getLocalMember.getSocketAddress.getAddress.getHostAddress)
    plateformManager = Some(maybeClustered.fold(PlatformLocator.factory.createPlatformManager())(host => PlatformLocator.factory.createPlatformManager(0, host)))
  }

  private[playvertx] def stop() = {
    plateformManager.map(_.stop())
    plateformManager = None
  }

  private[playvertx] def restart() = {
    stop()
    start()
  }

  def pm: Option[PlatformManager] = plateformManager

  def vertx: Option[Vertx] = plateformManager.map(_.vertx())

  def hz: Option[HazelcastInstance] = if (VertxConf.isClustered) {
    PlayHz.getInstance()
  } else None

}
