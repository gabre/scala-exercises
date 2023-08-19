package my.example
package dataprocessing

import dataprocessing.Cons.cons

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

case class Cons[A] private(h: () => A, t: () => LazyStream[A]) extends LazyStream[A] {
  override def equals(that: Any): Boolean = {
    that match {
      case that: Cons[A] =>
        this.h() == that.h() && this.t().equals(that.t())
      case _ => false
    }
  }
}

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

object LazyStream {
  def of[A](xs: A*): LazyStream[A] =
    xs.foldRight[LazyStream[A]](Nil)((x, acc) => cons(x, acc))
  def fromList[A](list: List[A]): LazyStream[A] =
    list.foldRight[LazyStream[A]](Nil)((x, acc) => cons(x, acc))
}
