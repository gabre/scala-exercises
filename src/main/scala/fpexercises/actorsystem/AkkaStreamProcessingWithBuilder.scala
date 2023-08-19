package my.example
package fpexercises.actorsystem

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._

object AkkaStreamProcessingWithBuilder {
  def minimal(): Unit = {
    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    val input1 = Source(List("apple", "cat", "banana", "pear"))
    val input2 = Source(List("cat", "dog", "banana", "apple"))

    val folded = input1.merge(input2).fold(Map[String, Int]()) { (acc, word) =>
      acc + (word -> (acc.getOrElse(word, 0) + 2))
    }

    val done = folded.runWith(Sink.foreach(println))
    done.onComplete(_ => system.terminate())(system.dispatcher)
  }

  def main(args: Array[String]) = {
    minimal()
  }

}
