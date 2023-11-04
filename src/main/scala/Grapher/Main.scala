package Grapher

import RandomWalker._
import NetGraphAlgebraDefs.{Action, NetGraphComponent, NodeObject}
import breeze.numerics.floor
import org.apache.spark
import org.apache.spark.{SparkConf, SparkContext}

import scala.math
import org.apache.spark.sql._
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.slf4j.LoggerFactory

import java.io.{FileInputStream, ObjectInputStream}

object Main extends Session {

  def main(args: Array[String]): Unit = {

    import spark.implicits._
    val orig: Graph[Boolean, Int] = build_graph("src/main/scala/Grapher/input/original.ngs")
    val origRDD: RDD[EdgeTriplet[Boolean, Int]] = orig.triplets
    val pert: Graph[Boolean, Int] = build_graph("src/main/scala/Grapher/input/perturbed.ngs")
    val pertRDD: RDD[EdgeTriplet[Boolean, Int]] = pert.triplets
    val nodes = node_mapping(pertRDD)
    val walks: RDD[(VertexId, Boolean, Int, List[Int])] =
    (1 to 12).foldLeft(spark.sparkContext.emptyRDD[(VertexId, Boolean, Int, List[Int])]) {
      (accumulated, _) => accumulated.union(walker(nodes))
    }
    val df = walks.toDF()
    df.show()
  }

  case class NetGraph(initState: NodeObject)

  def build_graph[VD, ED](filename: String): Graph[Boolean, Int] = {
    val deserialized = load(filename)
    val edges = spark.sparkContext.parallelize(edge_array(deserialized._1).map {
      case (src, dst, attr) => Edge(src, dst, attr)
    })
    val vertices = spark.sparkContext.parallelize(node_array(deserialized._2))
    val graph = Graph(vertices, edges)
    graph
  }

  def node_array(nodes: List[NodeObject]): Array[(Long, Boolean)] = {
    val node_array: Array[(Long, Boolean)] = nodes.map { node => (node.id.toLong, node.valuableData) }.toArray
    node_array
  }

  def edge_array(edges: List[Action]): Array[(Long, Long, Int)] = {
    val edge_array = edges.map { edge => (edge.fromNode.id.toLong, edge.toNode.id.toLong, calculate_cost(edge.cost)) }.toArray
    edge_array
  }

  def calculate_cost(cost: Double): Int = {
    val calc_cost: Int = floor(cost * 10).toInt
    if (calc_cost < 1) 1 else calc_cost
  }

  def load(filename: String): (List[Action], List[NodeObject]) = {
    val fis = new FileInputStream(filename)
    val ois = new ObjectInputStream(fis)
    val ng = ois.readObject.asInstanceOf[List[NetGraphComponent]]
    ois.close()
    fis.close()
    val nodes = ng.collect { case node: NodeObject => node }
    val edges = ng.collect { case edge: Action => edge }
    (edges, nodes)
  }
}



