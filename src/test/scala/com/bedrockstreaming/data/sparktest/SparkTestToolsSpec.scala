package com.bedrockstreaming.data.sparktest

import com.bedrockstreaming.data.sparktest.SparkTestTools.SparkSessionOps
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{ BooleanType, DoubleType, IntegerType, LongType, StringType }
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SparkTestToolsSpec extends AnyFlatSpec with Matchers with DataFrameEquality with SparkTestSupport {

  import spark.implicits._

  "createDF" should "create a DataFrame from a Seq of String" in {
    val data = Seq("Hey", "Ho")

    val result: DataFrame = spark.createDF(data, Seq(("columnName", StringType)))
    val expected = data.toDF("columnName")

    result shouldEqual expected
  }

  it should "create a DataFrame from a Seq of Tuple" in {
    val data = Seq(("Hey", 12), ("Ho", 3))

    val result: DataFrame = spark.createDF(data, Seq(("columnName1", StringType), ("columnName2", IntegerType)))
    val expected = data.toDF("columnName1", "columnName2")

    result shouldEqual expected
  }

  it should "create a DataFrame from a Seq of Tuple containing Null" in {

    val result: DataFrame = spark.createDF(
      Seq(("Hey", 12, 56L, false, 1.1), (null, null, null, null, null)),
      Seq(
        ("columnName1", StringType),
        ("columnName2", IntegerType),
        ("columnName3", LongType),
        ("columnName4", BooleanType),
        ("columnName5", DoubleType)
      )
    )
    val expected = Seq[(String, Integer, Option[Long], Option[Boolean], Option[Double])](
      ("Hey", 12, Some(56L), Some(false), Some(1.1)),
      (null, null, None, None, None)
    ).toDF("columnName1", "columnName2", "columnName3", "columnName4", "columnName5")

    result shouldEqual expected
  }
}
