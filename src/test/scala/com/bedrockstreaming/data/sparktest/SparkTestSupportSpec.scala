package com.bedrockstreaming.data.sparktest

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SparkTestSupportSpec extends AnyFlatSpec with Matchers with SparkTestSupport {

  override lazy val additionalSparkConfiguration: Map[String, String] =
    Map("spark.test.toto" -> "false", "spark.test.titi" -> "0")

  "spark" should "possess passed additional configuration" in {
    val sparkConf = spark.conf.getAll
    sparkConf.contains("spark.test.toto") shouldBe true
    sparkConf.contains("spark.test.titi") shouldBe true
    sparkConf.contains("spark.test.tata") shouldBe false
  }
}
