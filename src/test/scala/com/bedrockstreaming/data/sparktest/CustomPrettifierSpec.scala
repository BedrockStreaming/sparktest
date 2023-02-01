package com.bedrockstreaming.data.sparktest

import com.bedrockstreaming.data.sparktest.CustomPrettifier.prettyDataFrame
import com.bedrockstreaming.data.sparktest.SparkTestTools.SparkSessionOps
import org.apache.spark.sql.types.{ IntegerType, LongType, StringType }
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CustomPrettifierSpec extends AnyFlatSpec with Matchers with SparkTestSupport with CustomPrettifier {

  "prettyDataFrame" should "make a beautiful output for dataframe comparison" in {
    val dataframe = spark.createDF(
      Seq((1, "usera", 10L), (2, "userb", 0L), (3, "user3", 6541L)),
      Seq(("variant", IntegerType), ("uid", StringType), ("videos_viewed", LongType))
    )

    val result = prettyDataFrame(dataframe)

    val expected =
      """
        |
        |************************************
        |************** SCHEMA **************
        |************************************
        |
        |root
        | |-- uid: string (nullable = true)
        | |-- variant: integer (nullable = true)
        | |-- videos_viewed: long (nullable = true)
        |
        |
        |************************************
        |*************** DATA ***************
        |************************************
        |
        |+-----+-------+-------------+
        ||uid  |variant|videos_viewed|
        |+-----+-------+-------------+
        ||user3|3      |6541         |
        ||usera|1      |10           |
        ||userb|2      |0            |
        |+-----+-------+-------------+
        |
        |""".stripMargin

    result shouldEqual expected
  }
}
