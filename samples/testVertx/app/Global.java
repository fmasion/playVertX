import Verticles.BridgeServer;
import org.joda.time.DateTime;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.PlatformManager;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Akka;
import playvertx.VertX;
import scala.concurrent.duration.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		Logger.info("Application has started");
		if(VertX.pm().isDefined())
			deployBridge(VertX.pm().get());
		if(VertX.vertx().isDefined())
			connectEventBus(VertX.vertx().get());
	}

	@Override
	public void onStop(Application app) {
		if(playvertx.VertX.vertx().isDefined())
			undeploy(VertX.vertx().get());
		Logger.info("Application shutdown...");
	}

	private void deployBridge(PlatformManager pm) {
		Logger.info("Got Plateform manager");
		String clazz = BridgeServer.class.getName();
		pm.deployVerticle(clazz, null, new URL[]{}, 3, null, null);
	}

	private void connectEventBus(Vertx vertX){
		final EventBus bus =vertX.eventBus();
		Logger.info("Got event bus");
		// / Listening on EventBUS
		bus.registerHandler("some address", new Handler<Message<JsonObject>>() {
			public void handle(Message<JsonObject> msg) {
				Logger.info("Listening on bus : " + msg.body());
			}
		});
		// / Publishing on EventBus
		Akka.system()
				.scheduler()
				.schedule(Duration.create(2, TimeUnit.SECONDS),
						Duration.create(2, TimeUnit.SECONDS), () -> {
							Map<String, Object> jsonMap = new HashMap<>();
							jsonMap.put("text", "Hello, someaddress!" + DateTime.now());
							bus.publish("someaddress", new JsonObject(jsonMap));
						}, play.libs.Akka.system().dispatcher());
	}

	private void undeploy(Vertx vertx){
		vertx.stop();
	}

}
