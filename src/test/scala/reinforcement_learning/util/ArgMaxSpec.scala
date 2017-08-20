package reinforcement_learning.util

import org.scalatest.{FlatSpec, Matchers}

class ArgMaxSpec extends FlatSpec with Matchers {
  "ArgMax" should "return argmax from a normal example" in {
    val m = Map("a" -> 1, "b" -> 2, "c" -> -1, "d" -> -2)
    ArgMax.argMax(m) should be("d")
  }
  it should "not fail on empty map" in {
    val m = Map[String, Int]()
    an [UnsupportedOperationException] should be thrownBy ArgMax.argMax(m)
  }
}
