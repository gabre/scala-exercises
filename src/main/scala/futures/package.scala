package my.example

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

package object futures {

  def bar(s: String): Int = s.length

  def foo(name: String, age: Int): Future[Int] = {
    for {
      result1 <- Future.successful(s"${name} is ${age}")
      result2 <- Future.successful(result1)
    } yield bar(result2)
  }

}
