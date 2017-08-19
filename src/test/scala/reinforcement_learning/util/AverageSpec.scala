package reinforcement_learning.util

import org.scalatest.{FlatSpec, Matchers}

class AverageSpec extends FlatSpec with Matchers {
  "Average" should "initialize correctly" in {
    Average() should be(Average())
  }
  it should "add a value correctly" in {
    Average() + 1 should be(Average(1, 1))
    Average() + 1 + 2 should be(Average(1.5, 2))
    Average() + 1 + 2 + 4.5 should be(Average(2.5, 3))
  }
}
