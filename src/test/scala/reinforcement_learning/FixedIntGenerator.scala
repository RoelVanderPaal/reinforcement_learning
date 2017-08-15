package reinforcement_learning

import reinforcement_learning.environments.IntGenerator

class FixedIntGenerator(values: Seq[Int]) extends IntGenerator {
  val iter = Iterator.continually(values).flatten

  override def nextInt(n: Int) = iter.next()
}
