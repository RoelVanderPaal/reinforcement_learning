package reinforcement_learning

trait Environment[State, Action] {
  def initialState: State

  def nextState(a: Action): (State, Boolean, Reward)

  def possibleActions(state: State): Set[Action]
}
