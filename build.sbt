name := "java-8-playground"

organization := "info.schleichardt"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.10.4"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "io.sphere.jvmsdk" %% "play-sdk" % "1.0.0-20140606-SNAPSHOT"
)

