package Grapher

import org.apache.spark.sql.SparkSession

trait Session {

  lazy val spark: SparkSession = {
    SparkSession
      .builder()
      .master("local[3]")
      .appName("mitm")
      .getOrCreate()
  }
}
