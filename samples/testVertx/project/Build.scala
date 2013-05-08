import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "testVertx"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    "playvertx"  % "playvertx_2.10" % "0.1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
    //resolvers += "Local Play Repository" at "file://Users/fred/Developpement/play-2.1.1/repository/local"
    resolvers += Resolver.url("Fred's GitHub Play Repository", url("http://fmasion.github.com/releases/"))(Resolver.ivyStylePatterns)
  )
  ///Users/fred/Developpement/play-2.1.0/repository/local

}
