val vertxVersion = "2.1.4"

val playHazelcastVersion = "3.2.3-play2.3"

val jacksonVersion = "2.2.2"

val nettyVersion = "4.0.21.Final"

resolvers += "bintray" at "http://dl.bintray.com/fmasion/maven"

libraryDependencies ++= Seq(
  // Add your project dependencies here,
  "io.vertx" % "vertx-platform" % vertxVersion,
  "io.vertx" % "vertx-core" % vertxVersion,
  "io.vertx" % "vertx-hazelcast" % vertxVersion,
  "playhazelcast"  % "playhazelcast_2.10" % playHazelcastVersion,
  "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
  "io.netty" % "netty-all" % nettyVersion
)

publishTo := Some("Fred's bintray" at "https://api.bintray.com/maven/fmasion/maven/playVertX")

publishMavenStyle := true

scalacOptions ++= Seq( "-deprecation", "-unchecked", "-feature" )
