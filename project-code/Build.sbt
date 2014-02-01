  name     := "playVertx"

  version  := "2.1M5"

  libraryDependencies ++= Seq(
    // Add your project dependencies here,
    "io.vertx" % "vertx-platform" % "2.1M5",
    "io.vertx" % "vertx-core" % "2.1M5",  
    "io.vertx" % "vertx-hazelcast" % "2.1M5", 
    "playhazelcast"  % "playhazelcast_2.10" % "2.6.6",
    "com.fasterxml.jackson.core" % "jackson-annotations" % "2.2.2",   
    "com.fasterxml.jackson.core" % "jackson-core" % "2.2.2",   
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2",   
    "io.netty" % "netty-all" % "4.0.14.Final" 
  )

  play.Project.playScalaSettings


