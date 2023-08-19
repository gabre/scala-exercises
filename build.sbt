ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "untitled",
    idePackagePrefix := Some("my.example"),
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test,
    libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.3.7",
    libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.32"
  )
