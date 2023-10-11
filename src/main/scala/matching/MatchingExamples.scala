package my.example
package matching

import scala.util.{Failure, Success, Try}
import scala.reflect.runtime.universe._
object MatchingExamples {
  val Bee = "2"
  val b = "2"
  def matchingTuples(t: (Int, String, Boolean)): String = t match {
    case (1, "2", false) => "one"
    case (a, `b`, _) => "two"
    case (a, Bee, _) => "three"
  }

  def expansion(l: List[String], s: Seq[String]): String = (l, s) match {
    case (List(a, rest @ _*), Seq(all @ _*)) => "one"
    case _                                   => "two"
  }

  // here _ is a shorthand for an existential type
  // https://stackoverflow.com/questions/15186520/scala-any-vs-underscore-in-generics
  def matchTry_1(t: Try[_]): String = t match {
    case Failure(exception) => "exception"
    case Success(value) => "value"
  }

  // forSome - existential type
  // NOT THE SAME AS:
  // class Foo[T <: List[Z forSome { type Z }]]
  def matchTry_1_v2[T <: List[Z] forSome { type Z }](t: Try[T]): String = t match {
    case Failure(exception) => "exception"
    case Success(value) => "value"
  }

  def matchTry_2[A](t: Try[A]): String = t match {
    case Failure(exception) => "exception"
    case Success(value) => "value"
  }

  // Typed pattern matching
  def matchType(a: Any) = a match {
    case i: Int => "Int!"
    case s: String => "String!"
    // Map[Int, Int] or Map[String, Int] does not matter, Scala will believe it (because of erasure)
    case m: Map[_, _] => "map"
    case _ => "Something else."
  }

  // https://stackoverflow.com/questions/12218641/what-is-a-typetag-and-how-do-i-use-it
  def matchTypeWithTag[A : TypeTag](a: A) = a match {
    case i: Int => "Int!"
    case s: String => "String!"
    case m: Map[_, _] => {
      // With the =:= type equality, we can differentiate between different Maps
      println(typeOf[A] =:= typeOf[Map[String, Int]])
    }
    case _ => "Something else."
  }

  def partialFunctionExample = {
    List(1, 2, 3, 4, "a", "b", ()).map {
      case i: Int => "1"
      case s: String => "2"
      // but () will fail, MatchError
    }
  }

  def main(args: Array[String]): Unit = {
    matchTypeWithTag(Map.empty[String, Int])
    matchTypeWithTag(Map.empty[String, String])
  }
}
