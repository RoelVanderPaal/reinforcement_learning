package reinforcement_learning.util

object ArgMax {
  def argMax[A, B](m: Iterable[(A, B)])(implicit o: Ordering[B]): A = m.reduce((m1, m2) => if (o.gt(m2._2, m1._2)) m2 else m1)._1
}
