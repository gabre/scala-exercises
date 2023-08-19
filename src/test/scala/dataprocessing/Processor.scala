package my.example
package dataprocessing

import dataprocessing.Processor.{drop, take, transmitter}

import org.scalatest.funsuite.AnyFunSuiteLike

class ProcessorTest extends AnyFunSuiteLike {
  test("Transmission") {
    val input = LazyStream.of(1, 2, 3, 4, 5)
    val output = transmitter(input)
    assert(output == input)
  }

  test("Drop more than available") {
    val input = LazyStream.of(1, 2, 3)
    val output = drop(4)(input)
    assert(output == LazyStream.of[Nothing]())
  }

  test("Drop some") {
    val input = LazyStream.of(1, 2, 3)
    val output = drop(2)(input)
    assert(output == LazyStream.of[Int](3))
  }

  test("Take more than available") {
    val input = LazyStream.of(1, 2, 3)
    val output = take(4)(input)
    assert(output == input)
  }

  test("Take some") {
    val input = LazyStream.of(1, 2, 3)
    val output = take(2)(input)
    assert(output == LazyStream.of[Int](1, 2))
  }
}
