package com.bedrockstreaming.data.sparktest

import com.bedrockstreaming.data.sparktest.DataFrameEquality.dfEquality
import com.bedrockstreaming.data.sparktest.SparkTestTools.SparkSessionOps

import org.apache.spark.sql.types.{ ArrayType, IntegerType, MapType, StringType }
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DataFrameEqualitySpec extends AnyFlatSpec with Matchers with SparkTestSupport with CustomPrettifier {
  "DataFrameEquality" should "return true when the dataframes are equals" in {
    val df = spark.createDF(Seq(("Bedrock", "Lyon")), Seq(("company", StringType), ("city", StringType)))

    dfEquality(df, df) shouldBe true
  }

  "DataFrameEquality" should "return false when the dataframes are not equals" in {
    val df = spark.createDF(Seq(("Bedrock", "Lyon")), Seq(("company", StringType), ("city", StringType)))

    val otherDf = spark.createDF(Seq((0, 1)), Seq(("numberZero", IntegerType), ("numberOne", IntegerType)))

    dfEquality(df, otherDf) shouldBe false
  }

  it should "works with column type: Map" in {
    val df = spark.createDF(
      Seq(("id1", Map("key1" -> "value1", "key2" -> "value2")), ("id2", Map("key3" -> "value3", "key0" -> "value0"))),
      Seq(("id", StringType), ("map_column", MapType(StringType, StringType)))
    )

    val otherDf = spark.createDF(
      Seq((Map("key0" -> "value0", "key3" -> "value3"), "id2"), (Map("key2" -> "value2", "key1" -> "value1"), "id1")),
      Seq(("map_column", MapType(StringType, StringType)), ("id", StringType))
    )

    dfEquality(df, otherDf) shouldBe true
  }

  it should "not find equality if Map types are different" in {
    val df = spark.createDF(
      Seq(("id1", Map("key1" -> "1", "key2" -> "2")), ("id2", Map("key2" -> "2", "key0" -> "0"))),
      Seq(("id", StringType), ("map_column", MapType(StringType, StringType)))
    )

    val otherDf = spark.createDF(
      Seq(("id1", Map("key1" -> 1, "key2" -> 2)), ("id2", Map("key2" -> 2, "key0" -> 0))),
      Seq(("id", StringType), ("map_column", MapType(StringType, IntegerType)))
    )

    dfEquality(df, otherDf) shouldBe false
  }

  "castMapsAsArrays" should "cast all first level Map[A, B] to a key-sorted Array[(A, B)]" in {
    // Given
    val df = spark.createDF(
      Seq(
        ("unchanged", 1, Map(24 -> 1, 12 -> 2), Map("b" -> "B", "a" -> "A"), Map("keyA" -> 1, "keyC" -> 3, "keyB" -> 2))
      ),
      Seq(
        ("id", StringType),
        ("number", IntegerType),
        ("mapInt", MapType(IntegerType, IntegerType)),
        ("mapString", MapType(StringType, StringType)),
        ("mapMix", MapType(StringType, IntegerType))
      )
    )

    // When
    val dfWithArrays = DataFrameEquality.castMapsAsArrays(df)

    // Then
    dfWithArrays.first.getAs[String]("id") shouldBe "unchanged"
    dfWithArrays.first.getAs[Int]("number") shouldBe 1
    dfWithArrays.first.getAs[Array[(Int, Int)]]("mapInt").toString shouldBe "WrappedArray([12,2], [24,1])"
    dfWithArrays.first.getAs[Array[(String, String)]]("mapString").toString shouldBe "WrappedArray([a,A], [b,B])"
    dfWithArrays
      .first
      .getAs[Array[(String, Int)]]("mapMix")
      .toString shouldBe "WrappedArray([keyA,1], [keyB,2], [keyC,3])"
  }
}
