package reinforcement_learning

import reinforcement_learning.util.{Average, RandomUtil, RandomUtilImpl}

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
        val (newState, newDone, reward) = environment.nextState(policy(state))
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

  def SARSA[State, Action](
                            environment: Environment[State, Action],
                            epsilon: Probability = 0.0,
                            gamma: Reward,
                            alpha: Reward,
                            iterations: Int)
                          (implicit randomUtil: RandomUtil = new RandomUtilImpl()): collection.Map[State, Action] = {
    val stateActionValues = new StateActionValues[State, Action]()

    for (_ <- 1 to iterations) {
      var state = environment.initialState
      var action = stateActionValues.eGreedyAction(state, environment.possibleActions(state), epsilon)
      var done = false
      while (!done) {
        val (newState, newDone, reward) = environment.nextState(action)
        val newAction = stateActionValues.eGreedyAction(newState, environment.possibleActions(newState), epsilon)
        stateActionValues.adjust(state, action)(v => {
          val newStateValue = if (newDone) 0.0 else gamma * stateActionValues.value(newState, newAction)
          val delta = alpha * (reward + newStateValue - v)
          v + delta
        })
        state = newState
        action = newAction
        done = newDone
      }
    }
    stateActionValues.greedyPolicy
  }


  def QLearning[State, Action](
                                environment: Environment[State, Action],
                                epsilon: Probability = 0.0,
                                gamma: Reward,
                                alpha: Reward,
                                iterations: Int)
                              (implicit randomUtil: RandomUtil = new RandomUtilImpl()): collection.Map[State, Action] = {
    val stateActionValues = new StateActionValues[State, Action]()

    for (_ <- 1 to iterations) {
      var state = environment.initialState
      var done = false
      while (!done) {
        val action = stateActionValues.eGreedyAction(state, environment.possibleActions(state), epsilon)
        val (newState, newDone, reward) = environment.nextState(action)
        stateActionValues.adjust(state, action)(v => {
          val newStateValue = if (newDone) 0.0 else gamma * stateActionValues.maxReward(newState)
          //            println(state, action, newState, reward, newStateValue, v, reward + newStateValue - v)
          v + alpha * (reward + newStateValue - v)
        })
        state = newState
        done = newDone
      }
      //      println("done")
    }
    //            println(stateActionValues.rewards)
    stateActionValues.greedyPolicy
  }

}
