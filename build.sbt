import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.ruiandrebatista",
      scalaVersion := "2.11.8"
    )),
    name := "fs2-extensions",
    libraryDependencies ++= Seq(
      fs2Core,
      scalaTest % Test,
      scalacheck % Test
    )
  )
