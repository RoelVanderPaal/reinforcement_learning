package reinforcement_learning

import reinforcement_learning.util.{IntGenerator, RandomIntGeneratorImpl}

trait Environment[State, Action] {
  def initialState: State

  def nextState(a: Action): (State, Boolean, Reward)

  def possibleActions(state: State): Set[Action]
}

case class KnownEnvironmentWrapper[State, Action](knownEnvironment: KnownEnvironment[State, Action])(implicit randomGenerator: IntGenerator = new RandomIntGeneratorImpl()) extends Environment[State, Action] {
  private var state: State = _
  private val r = scala.util.Random

  override def initialState: State = {
    state = knownEnvironment.nonTerminalStates(randomGenerator.nextInt(knownEnvironment.nonTerminalStates.length))
    state
  }


  override def nextState(a: Action): (State, Boolean, Reward) = {
    val stateToProbability: Map[State, Probability] = knownEnvironment.stateProbabilities(state, a)
    val newState = selectRandom(stateToProbability).get
    val done = !knownEnvironment.nonTerminalStates.contains(newState)
    state = newState
    (newState, done, knownEnvironment.reward(newState, newState, a))
  }

  private def selectRandom(m: Map[State, Probability]): Option[State] = {
    var acc = 0.0
    val choice = r.nextDouble()
    for ((k, v) <- m) {
      acc += v
      if (choice <= acc) {
        return Some(k)
      }
    }
    None
  }

  override def possibleActions(state: State): Set[Action] = knownEnvironment.possibleActions(state)
}
