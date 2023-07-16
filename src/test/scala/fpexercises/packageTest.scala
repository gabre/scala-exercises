package my.example
package fpexercises

import org.scalatest.funsuite.AnyFunSuiteLike

class packageTest extends AnyFunSuiteLike {
  test("last") {
    assert(Some(3) == last(List(1, 2, 3)), "multiple elements last")
    assert(Some(1) == last(List(1)), "single element last")
    assert(None == last(List()), "empty last")
  }

  test("nth") {
    assert(Some(1) == nth(List(1, 2, 3), 0))
    assert(Some(2) == nth(List(1, 2, 3), 1))
    assert(Some(3) == nth(List(1, 2, 3), 2))
    assert(None == nth(List(1, 2, 3), 3))

    assert(Some(1) == nth(List(1), 0))

    assert(None == nth(List(), 5))
  }

  test("length") {
    assert(3 == length(List(1, 2, 3)))
    assert(1 == length(List(1)))
    assert(0 == length(List()))
  }

  test("reverse") {
    assert(List(3, 2, 1) == reverse(List(1, 2, 3)))
    assert(List(1) == reverse(List(1)))
    assert(List() == reverse(List()))
  }
}
