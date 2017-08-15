package reinforcement_learning.environments

trait IntGenerator {
  def nextInt(n: Int): Int
}

class RandomIntGeneratorImpl extends IntGenerator {
  val r = scala.util.Random
  override def nextInt(n: Int) = r.nextInt(n)
}
