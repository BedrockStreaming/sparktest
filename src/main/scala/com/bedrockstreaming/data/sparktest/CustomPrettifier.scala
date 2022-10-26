package com.bedrockstreaming.data.sparktest

import com.bedrockstreaming.data.sparktest.CustomPrettifier.prettyDataFrame
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{ DataFrame, SparkShowString }
import org.scalactic.Prettifier

trait CustomPrettifier {

  implicit val customPrettifier: Prettifier = {
    case df: DataFrame => prettyDataFrame(df)
    case anythingElse  => Prettifier.default(anythingElse)
  }
}

object CustomPrettifier {

  private[sparktest] def prettyDataFrame(df: DataFrame): String = {
    val schemaTitle =
      """
        |************************************
        |************** SCHEMA **************
        |************************************
        |""".stripMargin
    val dataTitle =
      """
        |************************************
        |*************** DATA ***************
        |************************************
        |""".stripMargin

    val byColNameDF = df.select(df.columns.sorted.map(col): _*)
    val stringSchema = byColNameDF.schema.treeString
    val orderedDF = byColNameDF.orderBy(byColNameDF.columns.map(col): _*)
    val stringData = showString(orderedDF)

    Seq(schemaTitle, stringSchema, dataTitle, stringData).mkString(
      sys.props("line.separator"),
      sys.props("line.separator"),
      sys.props("line.separator")
    )
  }

  private def showString(
    df: DataFrame,
    numRows: Int = Int.MaxValue,
    truncate: Int = 0,
    vertical: Boolean = false
  ): String =
    SparkShowString(df, numRows, truncate, vertical)
}
