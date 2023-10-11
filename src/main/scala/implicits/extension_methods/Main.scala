package my.example
package implicits.extension_methods
object Main {
  def main(args: Array[String]) = {
    import implicits.extension_methods.MagicInt._
    val i = 1 orMaybe 2 orMaybe 3
    println(i)
  }
}
