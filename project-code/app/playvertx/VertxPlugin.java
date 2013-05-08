package playvertx;

import java.util.concurrent.Callable;

import play.Application;
import play.Logger;
import play.Play;
import play.Plugin;

public class VertxPlugin extends Plugin {

	public VertxPlugin(Application application) {

	}

	@Override
	public void onStart() {
		Logger.info("Starting VERTX");
		String playVersion = play.core.PlayVersion.current();
		if (Play.isDev() && playVersion == "2.1.1") { // Patch for Play 2.1.1
														// bug :
														// https://github.com/playframework/Play20/pull/839
			Logger.info("PLAY VERTX LAZY LOAD for 2.1.1");
			play.libs.Akka.future(new Callable<Integer>() {
				@Override
				public Integer call() {
					VertX.restart();
					// this integer isn't used but is required by superclass
					return 1;
				}
			});
		} else {
			VertX.restart();
		}
	}

	@Override
	public void onStop() {
		Logger.info("Stopping VERTX");
		VertX.shutdown();
	}

}
