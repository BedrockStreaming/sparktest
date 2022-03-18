ThisBuild / organization := "com.bedrockstreaming"
ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.12.11"

// ********
// Versions
// ********
val SparkVersion = "3.2.1"
val ScalacticVersion = "3.2.11"

// ************
// Dependencies
// ************
libraryDependencies += "org.scalactic" %% "scalactic" % ScalacticVersion
libraryDependencies += "org.scalatest" %% "scalatest" % ScalacticVersion % "test"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % SparkVersion,
  "org.apache.spark" %% "spark-sql" % SparkVersion
)
