package my.example
package implicits.adt_dsl.case_class.adt

sealed trait Chain[+A] {
  // - argument of a method is a contravariant position
  //   - val animalBox: Box[Animal] = new Box[Cat] // valid because `Cat <: Animal`
  //     animalBox.set(new Dog) // BAD: if `set` mutates `animalBox`, it also mutates `catBox` to no longer contain a `Cat` but a `Dog`
  // - the type itself is covariant A <: B => Chain[A] <: Chain[B]
  def +:[B >: A](other: B): Chain[B] = ConsChain(Node(other), this)
}

case class ConsChain[A](elem: Node[A], rest: Chain[A]) extends Chain[A]
case class Node[+A](content: A) extends Chain[A]

object Chain {
  implicit def wrap[A]: A => Chain[A] = Node.apply
}