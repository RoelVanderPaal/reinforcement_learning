package reinforcement_learning

import reinforcement_learning.util.{RandomUtil, RandomUtilImpl}

trait Environment[State, Action] {
  def initialState: State

  def nextState(a: Action): (State, Boolean, Reward)

  def possibleActions(state: State): Set[Action]
}

case class KnownEnvironmentWrapper[State, Action](knownEnvironment: KnownEnvironment[State, Action])
                                                 (implicit randomUtil: RandomUtil = new RandomUtilImpl()) extends Environment[State, Action] {
  private var state: State = _

  override def initialState: State = {
    state = knownEnvironment.nonTerminalStates(randomUtil.nextInt(knownEnvironment.nonTerminalStates.length))
    state
  }


  override def nextState(a: Action): (State, Boolean, Reward) = {
    val stateToProbability: Map[State, Probability] = knownEnvironment.stateProbabilities(state, a)
    val newState = randomUtil.selectRandom(stateToProbability)
    val done = !knownEnvironment.nonTerminalStates.contains(newState)
    state = newState
    (newState, done, knownEnvironment.reward(newState, newState, a))
  }

  override def possibleActions(state: State): Set[Action] = knownEnvironment.possibleActions(state)
}
