package reinforcement_learning.util

object ArgMax {
  def argMax[A, B](m: Iterable[(A, B)])(implicit o: Ordering[B]): Option[A] =
    m.find { case (_, b) => b == m.map(_._2).max }.map { case (a, _) => a }
}
