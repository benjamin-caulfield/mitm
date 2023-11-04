package Grapher

import org.apache.spark.SparkContext

import scala.util.Random
import scala.collection.mutable.ListBuffer
import util.control.Breaks._
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

object RandomWalker extends Session {

  def walker(nodes: RDD[((VertexId, Boolean), List[(VertexId, Boolean, Int)])]): RDD[(VertexId, Boolean, Int, List[Int])] = {
    val path = ListBuffer[(VertexId, Boolean, Int, List[Int])]()
    val start = nodes.takeSample(false, 1)
    val first_node_to_path: (VertexId, Boolean, Int, List[Int]) = start.headOption match {
      case Some(((id, bool), _)) => (id, bool, 0, List(0))
    }
    path += first_node_to_path
    var current_node = start

    breakable {
      for (_ <- 0 to 24) {
        val choices = current_edges(current_node)
        val costs: List[Int] = choices.collect {
          case (_, _, int) => int
        }.toList
        if (choices.isEmpty) {
          break
        } else {
          val chosen = choice(choices)
          if (!path.contains(chosen)) {
            val add_to_path: (VertexId, Boolean, Int, List[Int]) = (chosen._1, chosen._2, chosen._3, costs)
            path += add_to_path
            val lookup_key: (VertexId, Boolean) = (chosen._1, chosen._2)
            val next_edges_and_weights: List[(VertexId, Boolean, Int)] = nodes.lookup(lookup_key).flatMap(_.lift(1)).toList
            val next_node: Array[((VertexId, Boolean), List[(VertexId, Boolean, Int)])] = Array((lookup_key, next_edges_and_weights))
            current_node = next_node

          } else {
            break
          }
        }
        }
      }
      val rdd: RDD[(VertexId, Boolean, Int, List[Int])] = spark.sparkContext.parallelize(path.toList)
      rdd
    }


  def choice(choices: ListBuffer[(VertexId, Boolean, Int)]): (VertexId, Boolean, Int) = {
    val random = new Random()
    val choice = choices(random.nextInt(choices.length))
    choice
  }

  def node_mapping(graph: RDD[EdgeTriplet[Boolean, Int]]): RDD[((VertexId, Boolean), List[(VertexId, Boolean, Int)])] = {
    graph.map { link =>
      ((link.srcId, link.srcAttr), List((link.dstId, link.dstAttr, link.attr)))
    }
    .reduceByKey(_ ++ _)
  }

  def current_edges(node: Array[((VertexId, Boolean), List[(VertexId, Boolean, Int)])]): ListBuffer[(VertexId, Boolean, Int)] = {
    val list: ListBuffer[(VertexId, Boolean, Int)] = ListBuffer(node.flatMap { case (_, edges) =>
      edges.map { case (id, vd, int) =>
        (id, vd, int)
      }
    }: _*)
    list
  }

  def current_weights(node: Array[((VertexId, Boolean), List[(VertexId, Boolean, Int)])]): List[Int] = {
    val list: List[Int] = node.flatMap { case (_, weights) =>
      weights.map { case (_, _, ints) =>
        ints
      }
    }.toList
    list
  }
}










