  name     := "playVertx"

  version  := "2.1RC1"

  resolvers += Resolver.url("Fred's GitHub Play Repository", url("http://fmasion.github.com/releases/"))(Resolver.ivyStylePatterns)

  libraryDependencies ++= Seq(
    // Add your project dependencies here,
    "io.vertx" % "vertx-platform" % "2.1RC1",
    "io.vertx" % "vertx-core" % "2.1RC1",
    "io.vertx" % "vertx-hazelcast" % "2.1RC1",
    "playhazelcast"  % "playhazelcast_2.10" % "2.6.6",
    "com.fasterxml.jackson.core" % "jackson-annotations" % "2.2.2",   
    "com.fasterxml.jackson.core" % "jackson-core" % "2.2.2",   
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2",   
    "io.netty" % "netty-all" % "4.0.17.Final"
  )

  play.Project.playScalaSettings


