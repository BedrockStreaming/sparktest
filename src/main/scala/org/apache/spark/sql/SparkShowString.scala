package org.apache.spark.sql

object SparkShowString {

  def apply(df: DataFrame, numRows: Int = Int.MaxValue, truncate: Int = 0, vertical: Boolean = false): String =
    df.showString(numRows, truncate, vertical)
}
