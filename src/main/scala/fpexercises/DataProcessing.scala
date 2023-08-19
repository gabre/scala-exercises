package my.example
package fpexercises

import scalaz.std.lazylist.lazylistSyntax._

object DataProcessing {
  def plainScalaDataProcessing(): Unit = {
    val input1 = List("apple", "cat", "banana", "pear").to(LazyList)
    val input2 = List("cat", "dog", "banana", "apple").to(LazyList)
    val merged = input1 interleave input2
    val wordCount = merged.foldRight(Map[String, Int]()) { (word, acc) =>
      acc + (word -> (acc.getOrElse(word, 0) + 1))
    }
    wordCount.foreach { case (k, v) =>
      println(s"${k}: ${v}")
    }
  }

  def main(args: Array[String]) = {
    plainScalaDataProcessing()
  }
}
