ThisBuild / organization := "com.bedrocksreaming.data"
ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.12.11"

// ********
// Versions
// ********

val SparkVersion = "3.0.0"

// ************
// Dependencies
// ************
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.11"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % "test"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % SparkVersion,
  "org.apache.spark" %% "spark-sql" % SparkVersion,
)
