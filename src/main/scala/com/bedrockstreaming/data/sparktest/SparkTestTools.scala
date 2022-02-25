package com.bedrockstreaming.data.sparktest

import java.util

import org.apache.spark.sql.types.{ DataType, StructField, StructType }
import org.apache.spark.sql.{ DataFrame, Row, SparkSession }

import scala.collection.JavaConverters._

object SparkTestTools {

  implicit class SparkSessionOps(spark: SparkSession) {

    def createDF[U](data: Seq[U], structFields: Seq[(String, DataType)]): DataFrame =
      spark.createDataFrame(toRows(data), toStructType(structFields))

    private def toRows[U](data: Seq[U]): util.List[Row] =
      data
        .map {
          case p: Product => Row(p.productIterator.toList: _*)
          case a          => Row(a)
        }
        .toList
        .asJava

    private def toStructType(structFields: Seq[(String, DataType)]): StructType =
      StructType(structFields.map { case (name, structType) =>
        StructField(name, structType)
      })
  }
}
