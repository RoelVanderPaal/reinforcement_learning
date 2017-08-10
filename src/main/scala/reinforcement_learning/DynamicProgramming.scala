package reinforcement_learning

object DynamicProgramming {
  def iterativePolicyEvaluation[State, Action](environment: Environment[State, Action], policy: State => Map[Action, Double], discountRate: Double, maxDelta: Double) = {
    var values = environment.allStates.map(s => s -> 0.0).toMap

    var delta = Double.MaxValue

    while (delta > maxDelta) {
      val newValues = values ++ environment.nonTerminalStates.map(state => {
        val actionProbabilities = policy(state)
        state -> actionProbabilities.map { case (action, probability) =>
          probability * environment.stateProbabilities(state, action).map { case (newState, stateProbability)
          => stateProbability * (environment.reward(state, newState, action) + values(newState))
          }.sum
        }.sum
      }).toMap
      delta = environment.allStates.map(s => newValues(s) - values(s)).map(math.abs).max
      values = newValues
    }
    values
  }
}
