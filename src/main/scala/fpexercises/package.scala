package my.example

import scala.annotation.tailrec

// https://vegapit.com/article/functional-programming-exercises-scala-functions-lists
package object fpexercises {
  // 1. Write a function last that returns an Option containing the last element of the List if it exists
  @tailrec
  def last[A](xs: List[A]): Option[A] = {
    xs match {
      case head :: Nil => Some(head)
      case _ :: next => last(next)
      case Nil => None
    }
  }

  // 2. Write a function nth that returns an Option containing the element of the List at index i if it exists
  def nth[A](xs: List[A], index: Int): Option[A] = {
    @tailrec
    def nthP[X](xs: List[X], index: Int, current: Int): Option[X] = {
      xs match {
        case head :: rest =>
          if (index == current) {
            Some(head)
          } else {
            nthP(rest, index, current + 1)
          }
        case Nil => None
      }
    }
    nthP(xs, index, 0)
  }

  // 3. Write a function length that returns the length of a list
  def length[A](xs: List[A]): Int = {
    @tailrec
    def lengthP[X](xs: List[X], currentLength: Int): Int = {
      xs match {
        case _ :: rest => lengthP(rest, currentLength + 1)
        case Nil => currentLength
      }
    }
    lengthP(xs, 0)
  }

  // 4. Write a function reverse that returns the original List with its elements in reverse order
  def reverse[A](xs: List[A]): List[A] = {
    @tailrec
    def reverseP[X](xs: List[X], acc: List[X]): List[X] = {
      xs match {
        case head :: rest => reverseP(rest, head :: acc)
        case Nil => acc
      }
    }
    reverseP(xs, List())
  }
}
