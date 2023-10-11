package my.example
package implicits.adt_dsl.case_class

import implicits.adt_dsl.case_class.adt.{Chain, ConsChain, Node}
import implicits.adt_dsl.case_class.adt.Chain._

object Main {
  def main() = {
    val chain: Chain[Int] = 1 +: 1
    val node: Chain[Int] = Node(1)

    val full_chain: Chain[Int] = 1 +: 2 +: 3
  }

  def test1() = {
    val a = new Node(1)
    val content_a = a.content

    val b = Node("b")
    val content_b = b.content

    val c = ConsChain(a, b)
    val d = "a" +: 1
  }

}
