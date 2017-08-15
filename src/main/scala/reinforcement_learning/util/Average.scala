package reinforcement_learning.util

case class Average(avg: Double = 0, n: Int = 0) {
  def +(value: Double) = Average(this.avg + (value - avg) / (this.n + 1), this.n + 1)
}
