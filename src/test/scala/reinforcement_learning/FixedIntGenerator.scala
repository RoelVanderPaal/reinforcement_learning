package reinforcement_learning

import reinforcement_learning.util.IntGenerator

class FixedIntGenerator(values: Seq[Int]) extends IntGenerator {
  private val iter = Iterator.continually(values).flatten

  override def nextInt(n: Int): Int = iter.next()
}
