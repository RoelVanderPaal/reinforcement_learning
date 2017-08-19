package reinforcement_learning


trait KnownEnvironment[State, Action] {
  def allStates: List[State]

  def nonTerminalStates: List[State]

  def stateProbabilities(s: State, a: Action): Map[State, Probability]

  def reward(oldState: State, newState: State, a: Action): Reward

  def possibleActions(state: State): Set[Action]
}
