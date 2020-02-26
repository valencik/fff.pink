lazy val ScalaTestVersion = "3.0.8"

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "ca.valencik",
      scalaVersion := "2.13.1",
      version := "0.1.0-SNAPSHOT",
      coverageMinimum := 75
    )
  ),
  name := "lsystems",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % ScalaTestVersion % Test
  )
)
