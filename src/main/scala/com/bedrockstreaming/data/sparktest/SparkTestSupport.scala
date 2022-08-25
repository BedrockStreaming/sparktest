package com.bedrockstreaming.data.sparktest

import org.apache.spark.sql.SparkSession
import org.slf4j.event.Level

trait SparkTestSupport {

  lazy val appName: String = "SparkTest Session"
  lazy val logLevel: Level = Level.WARN
  lazy val shufflePartitions: Int = 2
  lazy val additionalSparkConfiguration: Map[String, String] = Map()

  private val sparkBuilder = SparkSession
    .builder()
    .master("local[*]")
    .appName(appName)
    .config("spark.sql.shuffle.partitions", shufflePartitions.toString)

  additionalSparkConfiguration.foreach { case (k, v) => sparkBuilder.config(k, v) }

  implicit val spark: SparkSession = sparkBuilder.getOrCreate()

  spark.sparkContext.setLogLevel(logLevel.name())
}
