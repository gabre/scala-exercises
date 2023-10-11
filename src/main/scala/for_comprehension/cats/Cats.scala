package my.example
package for_comprehension.cats

import cats.{ Monad => CatsMonad }
import scalaz.{ Monad => ScalaZMonad }
object Cats {
  import cats.syntax.functor._
  import cats.syntax.flatMap._
  def doForComprehension[A, M[_] : CatsMonad](monadicThing: M[A]): M[String] =
    for {
      item1 <- monadicThing
      _item2 <- monadicThing
    } yield item1.toString
}

object ScalaZ {
  import scalaz.syntax.monad._
  def doForComprehension[A, M[_] : ScalaZMonad](monadicThing: M[A]): M[String] =
    for {
      item1 <- monadicThing
      _item2 <- monadicThing
    } yield item1.toString
}

object ScalaZFunctor {
  import scalaz.syntax.functor._
  import scalaz.Functor
  def doForComprehension[A, M[_] : Functor](functorThing: M[A]): M[String] =
    functorThing.map(_.toString)
}
