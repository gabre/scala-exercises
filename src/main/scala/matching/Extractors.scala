package my.example
package matching

import scala.util.Try
import scala.jdk.CollectionConverters._


object Csv {
  def unapplySeq(csv: String): Option[Seq[Seq[String]]] = Try {
    csv.lines().iterator().asScala
      .map(_.split(",").toSeq)
      .toSeq
  }.toOption
}
object Main {
  def main(args: Array[String]): Unit = {
    val text =
      """a,b,c
        |1,2,3
        |4,5,6
        |7,8,9
        |""".stripMargin

    text match {
      case Csv(l1, l2, _*) => {
        println(l1)
        println(l2)
      }
      case _ =>
        println("Does not match.")
    }
  }
}
