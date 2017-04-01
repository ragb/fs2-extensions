import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.ruiandrebatista",
      scalaVersion := "2.11.8",
      crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.1"),
      licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
scalacOptions ++= Seq(
  "-deprecation",           
  "-encoding", "UTF-8",       // yes, this is 2 args
  "-feature",                
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",       
  "-Xlint",
  "-Yno-adapted-args",       
  "-Ywarn-dead-code",        // N.B. doesn't work well with the ??? hole
  "-Ywarn-numeric-widen",   
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import"     // 2.11 only
)
    )),
    name := "fs2-extensions",
    libraryDependencies ++= Seq(
      fs2Core,
      specs2Core % Test,
      specs2Scalacheck % Test
    )
  )
