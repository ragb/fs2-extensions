import sbt._

object Dependencies {
  lazy val fs2Core = "co.fs2" %% "fs2-core" % fs2Version
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  lazy val scalacheck = "org.scalacheck" %% "scalacheck" % "1.13.4"

  val fs2Version = "0.9.2"
}


