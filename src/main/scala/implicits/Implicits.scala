package my.example
package implicits

import scala.language.implicitConversions

object ImplicitTypeConversions {
  // 1. Implicit def
  implicit def floatMagic(f: Float): String = s"${f} is like magic!"
}

object ImplicitReceiverConversions {
  // 2. Implicit class
  // Extension Method
  implicit class ImplicitClass(i: Int) {
    def magic(): String = s"${i} is like magic!"
  }
}

package ImplicitParameters {
  object ImplicitParametricMethods {
    // 3. implicit parameter
    def printToString(line: String)(implicit newLine: String): String = {
      s"${line}${newLine}"
    }
  }

  object ImplicitParameters {
    // 4. implicit val
    implicit val plainOldNewline: String = "\n"
  }
}

object ViewBounds {
  // There's one situation where an implicit is both an implicit conversion and an implicit parameter.
  def doStuff[T](s: T)(implicit convert: T => Int): String = {
    val result: Int = 6 * s
    s"Result: ${result}"
  }

  // "View bounds are deprecated.."
  // https://stackoverflow.com/questions/65573414/view-bounds-are-deprecated-use-an-implicit-parameter-instead
  def doStuff2[T <% Int](s: T): String = {
    val result: Int = 6 * s
    s"Result: ${result}"
  }
}

// Or typeclass pattern
object ContextBounds {
  trait Magicable[T] {
    def doMagic(t: T): Int
  }
  def doStuff[T](s: T)(implicit magicable: Magicable[T]): String = {
    val result = magicable.doMagic(s) * 6
    s"Result: ${result}"
  }

  def doStuff2[T : Magicable](s: T): String = {
    val magicable = implicitly[Magicable[T]]
    val result = magicable.doMagic(s) * 6
    s"Result: ${result}"
  }
}

object TryThemAll {
  // 1. implicit type conversion
  // A simple case..
  def implicitTypeConversions: String = {
    import ImplicitTypeConversions._
    5.0f
  }

  // A view bound was a mechanism introduced in Scala to enable the use of some type A as if it were some type B.
  def implicitViewBounds = {
    import ViewBounds._

    implicit def boolToInt(t: Boolean) = if (t) 1 else 0

    doStuff(true)
  }

  // 2. extension method ie. implicit receiver conversion
  def implicitReceiverConversions: String = {
    import ImplicitReceiverConversions._
    5.magic()
  }


  // 3. implicit parameters
  def implicitParameters: String = {
    import implicits.ImplicitParameters.ImplicitParametricMethods._
    printToString("line")("???")
    locally {
      import implicits.ImplicitParameters.ImplicitParameters.plainOldNewline
      printToString("line")
    }
    locally {
      implicit val myNewLine: String = ";"
      printToString("line")
    }
  }

  // 4. implicit type class instances
  def implicitContextBounds: String = {
    import ContextBounds._

    implicit val magicableBool: Magicable[Boolean] = new Magicable[Boolean] {
      override def doMagic(t: Boolean): Int = if (t) 1 else 0
    }

    doStuff(true)
    doStuff2(false)
  }
}