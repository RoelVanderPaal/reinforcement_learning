package reinforcement_learning

object DynamicProgramming {
  def iterativePolicyEvaluation[State, Action](environment: KnownEnvironment[State, Action], policy: State => Map[Action, Probability], discountRate: Reward, maxDelta: Reward): Map[State, Probability] = {
    var values = environment.allStates.map(s => s -> 0.0).toMap

    var delta = Double.MaxValue

    while (delta > maxDelta) {
      val newValues = values ++ environment.nonTerminalStates.map(state => {
        state -> policy(state).map { case (action, probability) =>
          probability * environment.stateProbabilities(state, action).map { case (newState, stateProbability) =>
            stateProbability * (environment.reward(state, newState, action) + discountRate * values(newState))
          }.sum
        }.sum
      }).toMap
      delta = environment.allStates.map(s => newValues(s) - values(s)).map(math.abs).max
      values = newValues
    }
    values
  }

  def valueIteration[State, Action](environment: KnownEnvironment[State, Action], discountRate: Reward, maxDelta: Reward): Map[State, Probability] = {
    var values = environment.allStates.map(s => s -> 0.0).toMap

    var delta = Double.MaxValue

    while (delta > maxDelta) {
      val newValues = values ++ environment.nonTerminalStates.map(state =>
        state -> environment.possibleActions(state).map(action =>
          environment.stateProbabilities(state, action).map { case (newState, stateProbability) =>
            stateProbability * (environment.reward(state, newState, action) + discountRate * values(newState))
          }.sum
        ).max
      )
      delta = environment.allStates.map(s => newValues(s) - values(s)).map(math.abs).max
      values = newValues
    }

    values
  }
}
