package reinforcement_learning

object MonteCarlo {
  def firstVisitStateValueEstimation[State, Action](
                                                     environment: Environment[State, Action],
                                                     policy: State => Action,
                                                     discountRate: Reward,
                                                     iterations: Int
                                                   ) = {
    0
  }
}
