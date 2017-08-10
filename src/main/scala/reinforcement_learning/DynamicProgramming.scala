package reinforcement_learning

object DynamicProgramming {
  def iterativePolicyEvaluation[State, Action](game: Game[State, Action], policy: State => Map[Action, Double], discountRate: Double, maxDelta: Double) = {
    var values = game.allStates.map(s => s -> 0.0).toMap

    var delta = Double.MaxValue

    while (delta > maxDelta) {
      val newValues = values ++ game.nonTerminalStates.map(state => {
        val actionProbabilities = policy(state)
        state -> actionProbabilities.map { case (action, probability) =>
          probability * game.stateProbabilities(state, action).map { case (newState, stateProbability)
          => stateProbability * (game.reward(state, newState, action) + values(newState))
          }.sum
        }.sum
      }).toMap
      delta = game.allStates.map(s => newValues(s) - values(s)).map(math.abs).max
      values = newValues
    }
    values
  }
}
