package reinforcement_learning

import reinforcement_learning.util.Average

import scala.collection.mutable

object MonteCarlo {
  def firstVisitStateValueEstimation[State, Action](
                                                     environment: Environment[State, Action],
                                                     policy: State => Action,
                                                     discountRate: Reward,
                                                     iterations: Int
                                                   ) = {
    var valuePerState = mutable.Map[State, Average]()
    for (n <- 1 to iterations) {
      var state: State = environment.initialState
      val accumulatedRewardPerState = mutable.Map[State, Reward]()
      var done = false
      do {
        val action: Action = policy(state)
        var (newState, newDone, reward) = environment.nextState(action)
        accumulatedRewardPerState.getOrElseUpdate(state, 0.0)
        accumulatedRewardPerState.transform((_, v) => v + reward)
        state = newState
        done = newDone
      } while (!done)
      for (state <- accumulatedRewardPerState.keys) {
        val value = valuePerState.getOrElseUpdate(state, Average())
        valuePerState(state) = value + accumulatedRewardPerState(state)
      }
    }
    valuePerState.mapValues(_.avg)
  }
}
