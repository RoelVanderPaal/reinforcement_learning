package reinforcement_learning.util

trait IntGenerator {
  def nextInt(n: Int): Int
}

class RandomIntGeneratorImpl extends IntGenerator {
  private val r = scala.util.Random

  override def nextInt(n: Int): Int = r.nextInt(n)
}
