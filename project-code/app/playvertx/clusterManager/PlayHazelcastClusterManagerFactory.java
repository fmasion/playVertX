package playvertx.clusterManager;

import org.vertx.java.core.spi.VertxSPI;
import org.vertx.java.core.spi.cluster.ClusterManager;
import org.vertx.java.core.spi.cluster.ClusterManagerFactory;
import org.vertx.java.spi.cluster.impl.hazelcast.PlayHazelcastClusterManager;
import playHazelcast.api.*;
import scala.Option;

import com.hazelcast.core.HazelcastInstance;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class PlayHazelcastClusterManagerFactory implements ClusterManagerFactory {

	public static String getClazzName() {
		return PlayHazelcastClusterManagerFactory.class.getName();
	}

	@Override
	public ClusterManager createClusterManager(VertxSPI vertx) {
		Option<HazelcastInstance> ohz = PlayHz.getInstance();
		if (ohz.isDefined()) {
			return new PlayHazelcastClusterManager(vertx, ohz.get());
		}
		return null;
	}
}
