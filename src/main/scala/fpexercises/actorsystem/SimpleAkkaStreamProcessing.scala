package my.example
package fpexercises.actorsystem

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.Future
import scala.util.{Failure, Success}

object SimpleAkkaStreamProcessing {
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

  def variations(): Unit = {
    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    val input1 = Source(List("apple", "cat", "banana", "pear"))
    val input2 = Source(List("cat", "dog", "banana", "apple"))

    val folded: Source[Map[String, Int], NotUsed] =
      input1
        .merge(input2)
        .fold(Map[String, Int]())
        { (acc, word) =>acc + (word -> (acc.getOrElse(word, 0) + 2))}

    // 1. materialization with RunWith
    val done = folded.runWith(Sink.foreach(println))
    done.onComplete(_ => system.terminate())(system.dispatcher)

    // 2. creating a runnable graph and running that - to selects the left materializer
    val runnableGraph = folded.to(Sink.foreach(println))
    runnableGraph.run()

    // 3. runForeach that basically ands a runWith with Sink.forEach
    folded.runForeach(println)

    // 4. toMat lets you select the materializer
    val x = folded.map(map => map.get("apple"))
    val runnableGraph2 = x.toMat(Sink.collection(List.toFactory()))(Keep.right)
    val futureWithListOfValues : Future[List[Option[Int]]] = runnableGraph2.run()

    // 5. via
    val x2: Source[Map[String, Int], NotUsed] = folded
    val x3 = x2.viaMat(Flow[Map[String, Int]].map(map => map.getOrElse("apple", 0)))(Keep.left)
    val runnableGraph3 = x3.toMat(Sink.ignore)(Keep.right)
    val futureOfDone: Future[Done] = runnableGraph3.run()
  }

  def toMatExample(): Unit = {
    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    val input1 = Source(List("apple", "cat", "banana", "pear"))
    val input2 = Source(List("cat", "dog", "banana", "apple"))

    val merged: Source[String, NotUsed] = input1.merge(input2)
    val runnableGraph2 = merged.toMat(Sink.collection(List.toFactory()))(Keep.right)
    val future: Future[List[String]] = runnableGraph2.run()

    future.onComplete { result =>
      println(result)
      system.terminate()
    }(materializer.executionContext)
  }

  def materializerExample(): Unit = {
    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    val input1 = Source(List("apple", "cat", "banana", "pear"))
    val input2 = Source(List("cat", "dog", "banana", "apple"))

    // This is nearly surely not the right way to tell 
    val merged: Source[String, Done] = input1.merge(input2).mapMaterializedValue(NotUsed => Done)
    val runnableGraph2 = merged.toMat(Sink.collection(List.toFactory()))(Keep.right)
    val future: Future[List[String]] = runnableGraph2.run()

    future.onComplete { result =>
      println(result)
      system.terminate()
    }(materializer.executionContext)
  }

  def asynchBoundaries(): Unit = {
    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    val input1 = Source(List("apple", "cat", "banana", "pear"))
    val input2 = Source(List("cat", "dog", "banana", "apple"))

    val merged: Source[String, NotUsed] = input1.merge(input2)
    // Setting an async boundary and a buffer:
    val runnableGraph2 = merged
      .async
      .buffer(2, OverflowStrategy.backpressure)
      .toMat(Sink.collection(List.toFactory()))(Keep.right)
    val future: Future[List[String]] = runnableGraph2.run()

    future.onComplete { result =>
      println(result)
      system.terminate()
    }(materializer.executionContext)
  }

  def main(args: Array[String]) = {
    minimal()

    toMatExample()

    asynchBoundaries()
  }

}
