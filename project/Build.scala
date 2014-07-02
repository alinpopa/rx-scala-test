import sbt._
import Keys._
import sbt.{Build => SbtBuild}
import org.sbtidea.SbtIdeaPlugin._

object Build extends SbtBuild {
  val commonSettings = Seq(
    organization := "org.test",
    scalaVersion := "2.11.0",
    crossPaths := false,
    ideaExcludeFolders := ".idea" :: ".idea_modules" :: Nil,
    scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation"),
    javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),
    sbt.Keys.fork in Test := false,
    resolvers ++= Seq(
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
      "Apache Snapshots" at "https://repository.apache.org/content/repositories/snapshots/"
    ),
    libraryDependencies ++= Seq(
      "org.slf4j" % "slf4j-api" % "1.7.5",
      "ch.qos.logback" % "logback-classic" % "1.0.13",
      "org.scalatest" %% "scalatest" % "2.1.6" % "test",
      "org.scalamock" %% "scalamock-scalatest-support" % "3.1.1" % "test",
      "io.spray" %% "spray-client" % "1.3.1" % "compile",
//      "com.typesafe.play" %% "play-json" % "2.3.1" % "compile",
//      "com.typesafe.play" %% "play-ws" % "2.3.1" % "compile",
      "com.typesafe.akka" %% "akka-actor" % "2.3.2" % "compile",
      "com.netflix.rxjava" % "rxjava-scala" % "0.19.2",
      "com.netflix.rxjava" % "rxjava-apache-http" % "0.19.2"
    )
  )

  lazy val root = Project(
    id = "rx-scala-test",
    base = file("."),
    settings = commonSettings
  )
}

