package my.example
package case_classes

case class Example(why: String)

object CaseClasses {
  def main(args: Array[String]) = {
    val example = Example("Something")
    val copied = example.copy("Else")

    assert(!(example eq copied))
    assert(!(example equals copied))
  }
}
