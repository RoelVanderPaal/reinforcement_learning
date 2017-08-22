package reinforcement_learning

import reinforcement_learning.util.RandomUtil

class FixedRandomUtilImpl(values: Seq[Int]) extends RandomUtil {
  private val iter = Iterator.continually(values).flatten

  override def nextInt(n: Int): Int = iter.next()

  override def selectRandomWithProbability[K](m: Traversable[(K, Probability)]) = ???

  override def selectRandom[K](m: Traversable[K]) = ???
}
