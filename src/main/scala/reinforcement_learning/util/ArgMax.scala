package reinforcement_learning.util

object ArgMax {
  def argMax[A, B](m: Iterable[(A, B)])(implicit o: Ordering[B]): A = m.reduce((m1, m2) => m2)._1
}
