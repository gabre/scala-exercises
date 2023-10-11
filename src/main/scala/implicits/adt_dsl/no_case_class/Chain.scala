package my.example
package implicits.adt_dsl.no_case_class

sealed trait Chain[A] {
  // - if it ends with ':' then it is right associative
  // - other is ON THE LEFT HANDSIDE of This
  // - if I put this directly into the subclasses Node and ChainCons, then 1 +: 2 +: 3 won't work because
  //   the compiler thinks it needs to wrap the Chain 2 +: 3 with a node
  // - other has to be type of A and not Node[A]
  def +: (other: A): Chain[A] = ChainCons(Node(other), this)
}

// Not a case class so I have to implement/try more things :)
class Node[A](val content: A) extends Chain[A] {}

// Companion object
object Node {
  def apply[A](content: A): Node[A] = new Node[A](content)
  // This will have to be imported to be able to "decorate" simple int, bool etc values with Node
  implicit def applyNode[A]: A => Node[A] = apply(_)
}

class ChainCons[A](val node: Node[A], val rest: Chain[A]) extends Chain[A] {}

object ChainCons {
  def apply[A](node: Node[A], rest: Chain[A]): ChainCons[A] = new ChainCons[A](node, rest)
}
