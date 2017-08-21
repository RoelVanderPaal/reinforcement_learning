package reinforcement_learning

import reinforcement_learning.util.{ArgMax, Average, RandomUtil, RandomUtilImpl}

import scala.collection.mutable
import scala.util.Random

object MonteCarlo {
  def firstVisitStateValueEstimation[State, Action](
                                                     environment: Environment[State, Action],
                                                     policy: State => Action,
                                                     discountRate: Reward,
                                                     iterations: Int
                                                   ): Map[State, Probability] = {
    val valuePerState = mutable.Map[State, Average]()
    for (_ <- 1 to iterations) {
      var state = environment.initialState
      val accumulatedRewardPerState = mutable.Map[State, Reward]()
      var done = false
      do {
        val (newState, newDone, reward) = environment.nextState(policy(state))
        accumulatedRewardPerState.getOrElseUpdate(state, 0.0)
        accumulatedRewardPerState.transform((_, v) => v + reward)
        state = newState
        done = newDone
      } while (!done)
      for ((state, reward) <- accumulatedRewardPerState) {
        val value = valuePerState.getOrElseUpdate(state, Average())
        valuePerState(state) = value + reward
      }
    }
    valuePerState.mapValues(_.avg).toMap
  }

  def control[State, Action](
                              environment: Environment[State, Action],
                              iterations: Int,
                              epsilon: Probability = 0.0)
                            (implicit randomUtil: RandomUtil = new RandomUtilImpl()):
  (collection.Map[State, Action], collection.Map[State, collection.Map[Action, Average]]) = {
    def randomAction(state: State) = randomUtil.selectRandom(environment.possibleActions(state))

    val policy = mutable.Map[State, Action]()
    val valuePerStateAndAction = mutable.Map[State, mutable.Map[Action, Average]]()

    for (_ <- 1 to iterations) {
      val accumulatedRewardPerStateAndAction = mutable.Map[(State, Action), Reward]()
      var state = environment.initialState
      var action = randomAction(state)
      var done = false
      while (!done) {
        val (newState, newDone, reward) = environment.nextState(action)
        accumulatedRewardPerStateAndAction.getOrElseUpdate((state, action), 0.0)
        accumulatedRewardPerStateAndAction.transform((_, v) => v + reward)
        state = newState
        done = newDone
        if (!done) {
          val argMaxAction = policy.getOrElseUpdate(state, randomAction(state))
          action = if (epsilon > 0.0) {
            Policy.eGreedyPolicy(environment.possibleActions(state), epsilon, argMaxAction)
          } else {
            argMaxAction
          }
        }
      }
      for (((state, action), reward) <- accumulatedRewardPerStateAndAction) {
        val actionToAverage = valuePerStateAndAction.getOrElseUpdate(state, mutable.Map[Action, Average]())
        actionToAverage(action) = actionToAverage.getOrElseUpdate(action, Average()) + reward
        policy(state) = ArgMax.argMax(actionToAverage)
      }
    }

    (policy, valuePerStateAndAction)
  }

}
