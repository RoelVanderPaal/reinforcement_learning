package reinforcement_learning

import reinforcement_learning.util.{ArgMax, Average}

import scala.collection.mutable
import scala.util.Random

object MonteCarlo {
  def firstVisitStateValueEstimation[State, Action](
                                                     environment: Environment[State, Action],
                                                     policy: State => Action,
                                                     discountRate: Reward,
                                                     iterations: Int
                                                   ): Map[State, Probability] = {
    var valuePerState = mutable.Map[State, Average]()
    for (n <- 1 to iterations) {
      var state = environment.initialState
      val accumulatedRewardPerState = mutable.Map[State, Reward]()
      var done = false
      do {
        var (newState, newDone, reward) = environment.nextState(policy(state))
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
    valuePerState.mapValues(_.avg).toMap
  }

  def ES[State, Action](environment: Environment[State, Action], iterations: Int): (collection.Map[State, Action], collection.Map[(State, Action), Average]) = {
    def randomAction(state: State) = {
      val actions = environment.possibleActions(state)
      actions.toList(Random.nextInt(actions.size))
    }

    var policy = mutable.Map[State, Action]()
    // TODO use nested map to improve performance
    var valuePerStateAndAction = mutable.Map[(State, Action), Average]()

    for (n <- 1 to iterations) {
      val accumulatedRewardPerStateAndAction = mutable.Map[(State, Action), Reward]()
      var state = environment.initialState
      var action = randomAction(state)
      var done = false
      while (!done) {
        var (newState, newDone, reward) = environment.nextState(action)
        accumulatedRewardPerStateAndAction.getOrElseUpdate((state, action), 0.0)
        accumulatedRewardPerStateAndAction.transform((_, v) => v + reward)
        state = newState
        done = newDone
        if (!done) {
          action = policy.getOrElseUpdate(state, randomAction(state))
        }
      }
      for (stateAndAction <- accumulatedRewardPerStateAndAction.keys) {
        val value = valuePerStateAndAction.getOrElseUpdate(stateAndAction, Average())
        valuePerStateAndAction(stateAndAction) = value + accumulatedRewardPerStateAndAction(stateAndAction)
      }
      for (group <- valuePerStateAndAction.groupBy { case ((s, _), _) => s }) {
        val (state, v) = group
        policy(state) = ArgMax.argMax(v).get._2
      }
    }
    (policy, valuePerStateAndAction)
  }

}
