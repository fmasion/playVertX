import _root_.sbtbuildinfo.Plugin._

lazy val playVertX = (project in file("project-code")).enablePlugins(PlayScala)

lazy val playVertXSample = (project in file("samples/testVertx")).enablePlugins(PlayScala).settings(publishArtifact := false)

lazy val playVertXParent = (project in file(".")).aggregate(playVertX,playVertXSample)

version in ThisBuild := "2.1.4-play2.3"

publishArtifact := false

