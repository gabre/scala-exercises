package my.example

import scala.collection.WithFilter

package object for_comprehension {
  // There are no traits expressing different methods that a for-comprehension can handle
  // https://contributors.scala-lang.org/t/proposal-introduce-traits-for-for-comprehension/861/12

  // The best doc:
  // https://alvinalexander.com/scala/fp-book/how-to-write-class-used-in-for-expression/

  /*
  1 Data type defines 'foreach method' => allows for loops
    - both with single and multiple generators
    - emphasis on the word “loops” ie. simple Java-style use of a for loop, w.g. for (i <- ints) println(i)
  2 Data type defines only map => can be used in for expressions with only 1 generator
  3 Data type defines flatMap as well as map => multiple generators
  4 Data type defines withFilter => filter expressions
    - starting with an if within the for expression
  */
  case class ForeachType(inner: List[String]) {
    def foreach[U](doIt: String => U): Unit = {
      inner.foreach(doIt)
    }
  }

  def try_foreach_only(): Unit = {
    val myContainer = new ForeachType(List("a", "b", "c", "d", "e", "f", "g"))
    for (item <- myContainer) {
      val modified = "888 " + item
      println(modified)
    }

    for (item1 <- myContainer; item2 <- myContainer) {
      println(item1 + item2)
    }

    for {
      item1 <- myContainer
      item2 <- myContainer
    } println(item1 + item2)

    for {
      item1 <- myContainer
      item2 <- myContainer
    } {
      val together = "It is " + item1 + " and " + item2
      println(together)
    }

    // This won't work, THIS IS NOT A JAVA STYLE FOR-LOOP
//    for {
//      item1 <- myContainer
//      item2 <- myContainer
//    } yield item1
  }

  // For-expression
//  for {
//    p <- persons             // generator
//    n = p.name               // definition
//    if (n startsWith "To")   // filter
//  } yield
  case class MapType[A](inner: List[A]) {
    def map[U](f: A => U): MapType[U] = MapType(inner.map(f))
  }

  def try_map_only() = {
    val myContainer = MapType(List("a", "b"))

    for {
      i <- myContainer
    } yield (i + "..")

    // This will ask for flatMap
//    for {
//      i1 <- myContainer
//      i2 <- myContainer
//    } yield (i1 + i2)
  }

  case class FlatMapType[A](inner: List[A]) {
    def map[U](f: A => U): FlatMapType[U] = FlatMapType(inner.map(f))
    def flatMap[U](f: A => FlatMapType[U]): FlatMapType[U] = FlatMapType(inner.flatMap(f(_).inner))
  }

  def try_flatmap() = {
    val myContainer = FlatMapType(List("a", "b"))

    for {
      i <- myContainer
    } yield (i + "..")

    for {
      i1 <- myContainer
      i2 <- myContainer
      i3 <- myContainer
    } yield (i1 + i2 + i3)

    // withFilter
//    for {
//      i1 <- myContainer
//      i2 <- myContainer
//      i3 <- myContainer
//      if (i3.length > 3)
//    } yield (i1 + i2 + myContainer)
  }

  case class FullForExpressionType[A](inner: List[A]) { fullForExpr =>
    def map[U](f: A => U): FullForExpressionType[U] = FullForExpressionType(inner.map(f))

    def flatMap[U](f: A => FullForExpressionType[U]): FullForExpressionType[U] = FullForExpressionType(inner.flatMap(f(_).inner))

    def withFilter(p: A => Boolean): WithFilter[A, FullForExpressionType] =
      new WithFilter[A, FullForExpressionType] {
        override def map[B](f: A => B): FullForExpressionType[B] =
          FullForExpressionType(inner.withFilter(p).map(f))

        override def flatMap[B](f: A => IterableOnce[B]): FullForExpressionType[B] =
          FullForExpressionType(inner.withFilter(p).flatMap(f))

        override def foreach[U](f: A => U): Unit =
          inner.withFilter(p).foreach(f)

        override def withFilter(q: A => Boolean): WithFilter[A, FullForExpressionType] =
          fullForExpr.withFilter(a => p(a) && q(a))
      }
  }

  def try_for_expression() = {
    val myContainer = FullForExpressionType(List("a", "b"))

    for {
      i <- myContainer
    } yield (i + "..")

    for {
      i1 <- myContainer
      i2 <- myContainer
      i3 <- myContainer
    } yield (i1 + i2 + i3)

    for {
      i1 <- myContainer
      i2 <- myContainer
      i3 <- myContainer
      if (i3.length > 3)
    } yield (i1 + i2 + myContainer)
  }

}
