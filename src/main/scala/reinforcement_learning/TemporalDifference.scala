package reinforcement_learning

import scala.collection.mutable

object TemporalDifference {
  def tabularTD0[State, Action](
                                 environment: Environment[State, Action],
                                 policy: State => Action,
                                 discountRate: Reward,
                                 stepSize: Reward,
                                 iterations: Int,
                                 defaultValuePerState: Option[collection.Traversable[(State, Reward)]] = None
                               ): collection.Map[State, Reward] = {
    val valuePerState = mutable.Map[State, Reward]()
    if (defaultValuePerState.isDefined) {
      valuePerState ++= defaultValuePerState.get
    }
    for (_ <- 1 to iterations) {
      var state = environment.initialState
      var done = false
      do {
        val action = policy(state)
        val (newState, newDone, reward) = environment.nextState(action)
        if (!done) {
          val oldValue = valuePerState.getOrElseUpdate(state, 0.0)
          val newValue = valuePerState.getOrElseUpdate(newState, 0.0)
          valuePerState(state) += stepSize * (reward + discountRate * newValue - oldValue)
        }

        state = newState
        done = newDone
      } while (!done)
    }
    valuePerState

  }
}
