import sbt._

object Dependencies {
  lazy val fs2Core = "co.fs2" %% "fs2-core" % fs2Version
  lazy val specs2Core = "org.specs2" %% "specs2-core" % specs2Version
  lazy val specs2Scalacheck = "org.specs2" %% "specs2-scalacheck" % specs2Version

  val fs2Version = "0.9.4"
  val specs2Version = "3.8.9"
}


