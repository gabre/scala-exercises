package my.example
package case_object

import scala.annotation.tailrec

sealed trait List[+A] { self =>

  // Note: this is not tailrec.
  // The system has to calculate the recursive call to do the last Cons call.
  // @tailrec
  final def map_[B](f: A => B): List[B] =
    self match {
      case Cons(head, tail) => Cons(f(head), tail.map_(f))
      case Nil => Nil
    }

  final def map[B](f: A => B): List[B] =
    foldr[List[B]](Nil)((item, accumulator) => Cons(f(item), accumulator))

  // NOT @tailrec
  final def foldrNaive[B](z: B)(f: (A, B) => B): B = self match {
    case Cons(head, tail) => f(head, tail.foldrNaive(z)(f))
    case Nil => z
  }

  final def foldr[B](z: B)(f: (A, B) => B): B =
    self.reverse().foldl(z)((a, b) => f(b, a))

  @tailrec
  final def foldl[B](z: B)(f: (B, A) => B): B = self match {
    case Cons(head, tail) => tail.foldl(f(z, head))(f)
    case Nil => z
  }

  final def reverse(): List[A] =
    self.foldl[List[A]](Nil) { (accumulator, item) => Cons(item, accumulator) }
}
case class Cons[A](head: A, tail: List[A]) extends List[A]
case object Nil extends List[Nothing]