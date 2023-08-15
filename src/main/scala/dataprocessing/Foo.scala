package my.example
package dataprocessing

object Foo {
  val v : LazyStream[Int] = ???
  v match {
    case Cons(h, t) => ???
    case Nil => Nil
  }
}
