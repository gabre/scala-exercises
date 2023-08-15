package my.example
package dataprocessing

sealed trait LazyStream[+A] { self =>
  import dataprocessing.Cons.cons

  def foldRight[B](z: B)(f: (A, B) => B) : B =
    self match {
      case Cons(h, t) => f(h(), t().foldRight(z)(f))
      case Nil => z
    }

  def foldLeft[B](z: B)(f: (B, A) => B): B =
    self match {
      case Cons(h, t) => t().foldLeft(f(z, h()))(f)
      case Nil => z
    }

  def map[B](f: A => B): LazyStream[B] =
    self.foldRight[LazyStream[B]](Nil)((a, acc) => cons(f(a), acc))
}

case class Cons[A] private(h: () => A, t: () => LazyStream[A]) extends LazyStream[A]

object Cons {
  private def apply[A](h: () => A, t: () => LazyStream[A]): Cons[A] = new Cons(h, t)

  def cons[A](h: => A, t: => LazyStream[A]): Cons[A] = {
    lazy val h_ = h
    lazy val t_ = t
    new Cons(() => h_, () => t_)
  }
}

case object Nil extends LazyStream[Nothing] {

}

