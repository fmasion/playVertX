name     := "playVertx"

version  := "2.1.2"

resolvers += Resolver.url("Fred's GitHub Play Repository", url("http://fmasion.github.com/releases/"))(Resolver.ivyStylePatterns)

libraryDependencies ++= Seq(
  // Add your project dependencies here,
  "io.vertx" % "vertx-platform" % version.value,
  "io.vertx" % "vertx-core" % version.value,
  "io.vertx" % "vertx-hazelcast" % version.value,
  "playhazelcast"  % "playhazelcast_2.10" % "3.2.3-play2.2",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.2.2",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.2.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2",
  "io.netty" % "netty-all" % "4.0.17.Final"
)

play.Project.playScalaSettings


