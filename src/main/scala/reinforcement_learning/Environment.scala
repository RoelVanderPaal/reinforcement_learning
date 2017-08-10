package reinforcement_learning


trait Environment[State, Action] {
  type Probability = Double
  type Reward = Double

  def allStates: List[State]

  def nonTerminalStates: List[State]

  def stateProbabilities(s: State, a: Action): Map[State, Probability]

  def reward(oldState: State, newState: State, a: Action): Reward
}
