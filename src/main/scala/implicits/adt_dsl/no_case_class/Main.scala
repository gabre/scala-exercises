package my.example
package implicits.adt_dsl.no_case_class

import implicits.adt_dsl.no_case_class.Node.applyNode

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

    val c = ChainCons(a, b)
    // val d: Chain[Any] = 1 +: "b"
  }

}
