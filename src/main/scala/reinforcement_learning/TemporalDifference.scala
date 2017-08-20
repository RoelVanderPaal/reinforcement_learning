package reinforcement_learning

import scala.collection.mutable

object TemporalDifference {
  def tabularTD0[State, Action](
                                 environment: Environment[State, Action],
                                 policy: State => Action,
                                 discountRate: Reward,
                                 stepSize: Reward,
                                 iterations: Int
                               ): collection.Map[State, Reward] = {
    val valuePerState = mutable.Map[State, Reward]()
    for (_ <- 1 to iterations) {
      var state = environment.initialState
      var done = false
      do {
        val (newState, newDone, reward) = environment.nextState(policy(state))
        val oldValue = valuePerState.getOrElseUpdate(state, 0.0)
        val newValue = if (done) valuePerState.getOrElseUpdate(newState, 0.0) else 0.0

        valuePerState(state) = oldValue + stepSize * (reward + discountRate * newValue - oldValue)
        state = newState
        done = newDone
      } while (!done)
    }
    valuePerState

  }
}
