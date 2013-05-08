import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "playVertx"
  val appVersion      = "0.1"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "org.vert-x" % "vertx-core" % "1.3.1.final"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    //publishArtifact in(Compile, packageDoc) := false      
  )

}
