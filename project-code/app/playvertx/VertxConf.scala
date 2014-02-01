package playvertx

import play.api.Play
import playHazelcast.api.HazelcastPlugin
import playHazelcast.api.PlayHz
import play.api.Logger
import playvertx.clusterManager.PlayHazelcastClusterManagerFactory

object VertxConf {
  val log = Logger("VertxConf")

  lazy val isClustered = useHazelCastPlugin

  private def init = {
    val threadPoolSize = Play.current.configuration.getInt("playVertX.threadPoolSize").getOrElse("0")
    if (threadPoolSize != 0) System.setProperty("vertx.pool.eventloop.size", "" + threadPoolSize) else System.clearProperty("vertx.pool.eventloop.size")

//    val workerThreadPoolSize = Play.current.configuration.getInt("playVertX.workerThreadPoolSize").getOrElse("0")
//    if (workerThreadPoolSize != 0) System.setProperty("vertx.pool.worker.size", "" + workerThreadPoolSize) else System.clearProperty("vertx.pool.worker.size")

    if (useHazelCastPlugin) {
      val clazz = PlayHazelcastClusterManagerFactory.getClazzName
      System.setProperty("vertx.clusterManagerFactory", clazz)
    } else {
      System.clearProperty("vertx.clusterManagerFactory")
    }

  }
  
  def useHazelCastPlugin: Boolean = {
    def isHazelCastPluginActive: Boolean = Play.current.plugin[HazelcastPlugin].isDefined && PlayHz.getInstance.isDefined
    Play.current.configuration.getBoolean("playVertX.isClustered").getOrElse(false) && isHazelCastPluginActive
  }

  init

}