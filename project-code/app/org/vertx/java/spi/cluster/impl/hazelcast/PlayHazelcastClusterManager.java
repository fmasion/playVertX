/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vertx.java.spi.cluster.impl.hazelcast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hazelcast.core.*;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;
import org.vertx.java.core.spi.VertxSPI;
import org.vertx.java.core.spi.cluster.AsyncMap;
import org.vertx.java.core.spi.cluster.AsyncMultiMap;
import org.vertx.java.core.spi.cluster.ClusterManager;
import org.vertx.java.core.spi.cluster.NodeListener;
import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;

/**
 * A cluster manager that uses Hazelcast
 * 
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class PlayHazelcastClusterManager implements ClusterManager, MembershipListener {

  private static final Logger log = LoggerFactory.getLogger(PlayHazelcastClusterManager.class);
  // Hazelcast config file

  private static final String DEFAULT_CONFIG_FILE = "default-cluster.xml";
  private static final String CONFIG_FILE = "cluster.xml";
  
  private final VertxSPI vertx;

  private HazelcastInstance hazelcast;
  private String nodeID;
  private NodeListener nodeListener;
  private String membershipListenerId;

  private boolean active;

  /**
   * Constructor
   */
  public PlayHazelcastClusterManager(final VertxSPI vertx, HazelcastInstance hazelcast ) {
  	this.vertx = vertx;
  	this.hazelcast = hazelcast;
    // We have our own shutdown hook and need to ensure ours runs before Hazelcast is shutdown
    System.setProperty("hazelcast.shutdownhook.enabled", "false");
  }

  public synchronized void join() {
	log.info("Starting Custom Cluster Manager yeepeekai");  
    if (active) {
      return;
    }
    nodeID = hazelcast.getCluster().getLocalMember().getUuid();
    membershipListenerId = hazelcast.getCluster().addMembershipListener(this);

    active = true;
  }

	/**
	 * Every eventbus handler has an ID. SubsMap (subscriber map) is a MultiMap which
	 * maps handler-IDs with server-IDs and thus allows the eventbus to determine where
	 * to send messages.
	 *
	 * @param name A unique name by which the the MultiMap can be identified within the cluster.
	 *     See the cluster config file (e.g. cluster.xml in case of HazelcastClusterManager) for
	 *     additional MultiMap config parameters.
	 * @return subscription map
	 */
  public <K, V> AsyncMultiMap<K, V> getAsyncMultiMap(final String name) {
    com.hazelcast.core.MultiMap map = hazelcast.getMultiMap(name);
    return new HazelcastAsyncMultiMap(vertx, map);
  }

  @Override
  public String getNodeID() {
    return nodeID;
  }

  @Override
  public List<String> getNodes() {
    Set<Member> members = hazelcast.getCluster().getMembers();
    List<String> lMembers = new ArrayList<>();
    for (Member member: members) {
      lMembers.add(member.getUuid());
    }
    return lMembers;
  }

  @Override
  public void nodeListener(NodeListener listener) {
    this.nodeListener = listener;
  }

  @Override
  public <K, V> AsyncMap<K, V> getAsyncMap(String name) {
    IMap<K, V> map = hazelcast.getMap(name);
    return new HazelcastAsyncMap(vertx, map);
  }

  @Override
  public <K, V> Map<K, V> getSyncMap(String name) {
    IMap<K, V> map = hazelcast.getMap(name);
    return map;
  }

  public synchronized void leave() {
    if (!active) {
      return;
    }
    hazelcast.getCluster().removeMembershipListener(membershipListenerId);
 		hazelcast.getLifecycleService().shutdown();
    active = false;
  }

  @Override
  public synchronized void memberAdded(MembershipEvent membershipEvent) {
    if (!active) {
      return;
    }
    try {
      if (nodeListener != null) {
        Member member = membershipEvent.getMember();
        nodeListener.nodeAdded(member.getUuid());
      }
    } catch (Throwable t) {
      log.error("Failed to handle memberAdded", t);
    }
  }

  @Override
  public synchronized void memberRemoved(MembershipEvent membershipEvent) {
    if (!active) {
      return;
    }
    try {
      if (nodeListener != null) {
        Member member = membershipEvent.getMember();
        nodeListener.nodeLeft(member.getUuid());
      }
    } catch (Throwable t) {
      log.error("Failed to handle memberRemoved", t);
    }
  }

  @Override
  public void memberAttributeChanged(MemberAttributeEvent memberAttributeEvent) {
  }

  private InputStream getConfigStream() {
    InputStream is = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
    if (is == null) {
      is = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE);
    }
    return is;
  }

  /**
   * Get the Hazelcast config
   * @return a config object
   */
  private Config getConfig() {
    Config cfg = null;
    try (InputStream is = getConfigStream();
         InputStream bis = new BufferedInputStream(is)) {
      if (is != null) {
        cfg = new XmlConfigBuilder(bis).build();
      }
    } catch (IOException ex) {
      log.error("Failed to read config", ex);
    }
    return cfg;
  }

}
