package com.bedrockstreaming.data.sparktest

import com.bedrockstreaming.data.sparktest.DataFrameEquality.dfEquality
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{ array_sort, col, udf }
import org.apache.spark.sql.types.{ MapType, StringType, StructField }
import org.scalactic.Equality

trait DataFrameEquality extends CustomPrettifier {

  implicit val dataFrameEquality: Equality[DataFrame] = new Equality[DataFrame] {

    override def areEqual(left: DataFrame, other: Any): Boolean =
      other match {
        case right: DataFrame => dfEquality(left, right)
        case _                => false
      }
  }
}

object DataFrameEquality {

  private[sparktest] def dfEquality(left: DataFrame, right: DataFrame): Boolean = {
    val leftDF = left.select(left.columns.sorted.map(col): _*).persist
    val rightDF = right.select(right.columns.sorted.map(col): _*).persist
    // TODO: temporary workaround, waiting for Spark 3.0.1 (https://issues.apache.org/jira/browse/SPARK-32372)
    val leftDFClone = leftDF.toDF(left.columns.sorted: _*)
    val rightDFClone = rightDF.toDF(right.columns.sorted: _*)

    val leftSchema = leftDF.dtypes
    val rightSchema = rightDF.dtypes

    leftSchema.diff(rightSchema).isEmpty &&
    rightSchema.diff(leftSchema).isEmpty &&
    leftDF.count() == rightDF.count() &&
    castMapsAsArrays(leftDF)
      .union(castMapsAsArrays(rightDF))
      .except(castMapsAsArrays(leftDFClone).intersect(castMapsAsArrays(rightDFClone)))
      .isEmpty
  }

  private val mapToArrayUdf = udf((m: Map[String, String]) => m.toArray.sorted)

  private[sparktest] def castMapsAsArrays(df: DataFrame): DataFrame =
    df.schema
      .foldLeft(df)((castedDf: DataFrame, structField: StructField) =>
        structField.dataType match {
          case _: MapType =>
            castedDf.withColumn(structField.name, mapToArrayUdf(col(structField.name)))
          case _ => castedDf
        }
      )
}
