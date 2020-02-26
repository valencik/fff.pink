lazy val betterFilesVersion = "3.8.0"
lazy val scalaTestVersion   = "3.1.1"

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
    "com.github.pathikrit" %% "better-files" % betterFilesVersion,
    "org.scalatest"        %% "scalatest"    % scalaTestVersion % Test
  )
)
