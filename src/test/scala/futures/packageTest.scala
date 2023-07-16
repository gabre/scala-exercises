package my.example
package futures

import org.scalatest.time.SpanSugar.convertIntToGrainOfTime

import scala.concurrent.Await

class packageTest extends org.scalatest.funsuite.AnyFunSuiteLike {
  test("foo does its thing") {
    val fooFuture = foo("Albert", 26)
    assert(12 == Await.result(fooFuture, 1.second), "foo did its thing")
  }
}
