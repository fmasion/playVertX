  name     := "playVertx"

  version  := "0.2.0"

  libraryDependencies ++= Seq(
    // Add your project dependencies here,
    "io.vertx" % "vertx-platform" % "2.1M1",
    "io.vertx" % "vertx-core" % "2.1M1",  
    "io.vertx" % "vertx-hazelcast" % "2.1M1", 
    "playhazelcast"  % "playhazelcast_2.10" % "0.2.0",
    "com.fasterxml.jackson.core" % "jackson-annotations" % "2.2.2",   
    "com.fasterxml.jackson.core" % "jackson-core" % "2.2.2",   
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2",   
    "io.netty" % "netty-all" % "4.0.11.Final" 
  )

  play.Project.playScalaSettings


