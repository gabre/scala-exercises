package my.example
package implicits.extension_methods

import java.util.Random

object MagicInt {
  implicit class MagicInt(val i: Int) {
    def orMaybe(i2: Int): Int = if (new Random().nextInt() % 2 == 0) i else i2
  }

}
