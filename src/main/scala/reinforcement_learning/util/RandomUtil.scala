package reinforcement_learning.util

import reinforcement_learning.Probability

trait RandomUtil {
  def nextInt(n: Int): Int

  def selectRandom[K](m: Traversable[(K, Probability)]): K
}

class RandomUtilImpl extends RandomUtil {
  private val r = scala.util.Random

  override def nextInt(n: Int): Int = r.nextInt(n)

  override def selectRandom[K](m: Traversable[(K, Probability)]): K = {
    var acc = 0.0
    val choice = r.nextDouble()
    for ((k, v) <- m) {
      acc += v
      if (choice <= acc) {
        return k
      }
    }
    throw new RuntimeException("sum of probabilities is not 1")
  }
}
