package playvertx;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.impl.DefaultVertx;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;

import play.Logger;
import play.Play;

public class VertX {

	private static volatile VertX INSTANCE = null;
	private static Vertx vertx = null;
	private static int clusterPort = 25001;
	private static String clusterHost = null;
	private static int httpPort = 8080;
	private static String eventBusUrlPrefix = "/eventbus";

	private VertX() {
		Integer tmpPort = Play.application().configuration().getInt("playVertX.clusterPort");
		if (tmpPort != null) {
			clusterPort = tmpPort.intValue();
			Logger.info("Using cluster-port : " + clusterPort);
		} else {
			Logger.info("No cluster-port specified so using : " + clusterPort);
		}

		clusterHost = Play.application().configuration().getString("playVertX.clusterHost");

		if (clusterHost == null) {
			clusterHost = getDefaultAddress();
			if (clusterHost == null) {
				Logger.error("Unable to find a default network interface for clustering. Please specify one using playVertX.clusterHost in application.conf");
				return;
			} else {
				Logger.info("No cluster-host specified so using address " + clusterHost);
			}
		}

		vertx = new DefaultVertx(clusterPort, clusterHost);

		Integer tmpHttpPort = Play.application().configuration().getInt("playVertX.httpPort");
		if (tmpHttpPort != null) {
			httpPort = tmpHttpPort.intValue();
		} else {
			Logger.info("No HTTP-port specified you can specify one using playVertX.httpPort in application.conf using default : " + httpPort);
		}

		bootstrap();
	}

	public void bootstrap() {
		HttpServer server = vertx.createHttpServer();
		server.requestHandler(new Handler<HttpServerRequest>() {
			public void handle(HttpServerRequest req) {
				Logger.debug("Got request: " + req.uri);
				Logger.debug("Headers are: ");
				for (String key : req.headers().keySet()) {
					Logger.debug(key + ":" + req.headers().get(key));
				}
				req.response.headers().put("Content-Type", "text/html; charset=UTF-8");
				req.response.end("<html><body><h1>Hello from playVertX!</h1></body></html>");
			}
		});

		String tmpUrlPrefix = Play.application().configuration().getString("playVertX.eventBusUrlPrefix");
		if (tmpUrlPrefix == null) {
			Logger.info("You can specify url prefix using playVertX.urlPrefix in application.conf");
		}

		JsonArray permitted = new JsonArray();
		permitted.add(new JsonObject()); // Let everything through
		SockJSServer sockJSServer = vertx.createSockJSServer(server);
		sockJSServer.bridge(new JsonObject().putString("prefix", eventBusUrlPrefix), permitted, permitted);
		server.listen(httpPort);
		Logger.info("Event bus created with on port : " + httpPort + " and prefix : " + eventBusUrlPrefix);
		play.Logger.info("Starting vertX plugin's http server ");
	}

	private String getDefaultAddress() {
		Enumeration<NetworkInterface> nets;
		try {
			nets = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			return null;
		}
		NetworkInterface netinf;
		while (nets.hasMoreElements()) {
			netinf = nets.nextElement();

			Enumeration<InetAddress> addresses = netinf.getInetAddresses();

			while (addresses.hasMoreElements()) {
				InetAddress address = addresses.nextElement();
				if (!address.isAnyLocalAddress() && !address.isMulticastAddress() && !(address instanceof Inet6Address)) {
					return address.getHostAddress();
				}
			}
		}
		return null;
	}

	public synchronized static VertX getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new VertX();
		}
		return INSTANCE;
	}

	public static void restart() {
		shutdown();
		getInstance();
	}

	public synchronized static void shutdown() {
		if (INSTANCE != null) {
			INSTANCE.vertx.stop();
			INSTANCE = null;
			vertx = null;
		}
	}

	public static EventBus getEventBus() {
		getInstance(); // use to ensure async and sync mode with the
						// synchronized singleton (for 2.1.1 bug)
		return vertx.eventBus();
	}

}
