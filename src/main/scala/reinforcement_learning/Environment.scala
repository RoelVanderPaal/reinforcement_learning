package reinforcement_learning

trait Environment[State, Action] {
  def initialState: State

  def nextState(a: Action): (State, Boolean, Reward)
}
