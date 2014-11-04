libraryDependencies ++= Seq(
  // Add your project dependencies here,
  javaCore,
  "playvertx"  % "playvertx_2.10" % "2.1.4-play2.3"
)

resolvers += "bintray" at "http://dl.bintray.com/fmasion/maven"
