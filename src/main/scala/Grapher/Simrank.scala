package Grapher

import Grapher.Main.build_graph
import RandomWalker._
import jdk.nashorn.internal.objects.NativeJava.extend
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{EdgeTriplet, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import spire.syntax.bool

import scala.reflect.internal.util.NoPosition.source

object Simrank extends Session {

  import spark.implicits._

  val orig: Graph[Boolean, Int] = build_graph("src/main/scala/Grapher/input/original.ngs")
  val origRDD: RDD[EdgeTriplet[Boolean, Int]] = orig.triplets

  def main(args: Array[String]): Unit = {
    val mapped_nodes: RDD[(VertexId, Boolean), Int)] = origRDD.map { row =>
      (row.srcId, (row.attr, row.srcAttr))
    }
    val reduced_nodes: RDD[((VertexId, Boolean), Iterable[List[Int]])] = mapped_nodes.groupByKey()
    val ten = reduced_nodes.take(10)
    ten.foreach(println)
  }

















}
