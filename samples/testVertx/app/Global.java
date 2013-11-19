import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.PlatformManager;

import Verticles.BridgeServer;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import playvertx.VertX;
import playvertx.clusterManager.PlayHazelcastClusterManager;
import scala.concurrent.duration.Duration;
import scala.Option;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		Logger.info("TESTVERTX : Application has started");
		PlayHazelcastClusterManager clusterManager; 
		PlatformManager pm = VertX.pm().get();
		String clazz = BridgeServer.class.getName();
		pm.deployVerticle(clazz, null, new URL[] {}, 3, null, null);
		
		final EventBus bus = getEventBus();
		Logger.info("TESTVERTX : Got EVENT BUS");

		// / Listening on EventBUS
		bus.registerHandler("someaddress", new Handler<Message<JsonObject>>() {
			public void handle(Message<JsonObject> msg) {
				Logger.info("Listening on bus : " + msg.body());
			}
		});

		// / Publishing on EventBus
		play.libs.Akka
				.system()
				.scheduler()
				.schedule(Duration.create(2, TimeUnit.SECONDS),
						Duration.create(2, TimeUnit.SECONDS), new Runnable() {
							@Override
							public void run() {
								Map<String, Object> jsonMap = new HashMap<String, Object>();
								jsonMap.put("text", "Hello, someaddress!"
										+ DateTime.now());
								bus.publish("someaddress", new JsonObject(
										jsonMap));

							}
						}, play.libs.Akka.system().dispatcher());

	}

	@Override
	public void onStop(Application app) {
		Logger.info("testvertx : Application shutdown...");
	}

	public EventBus getEventBus() {
		return VertX.vertx().get().eventBus();
	}

}
