package reinforcement_learning.known_environments

import reinforcement_learning.{KnownEnvironment, Probability, Reward}

case class GamblersProblemEnvironment(probability_head: Double) extends KnownEnvironment[Int, Int] {
  override def allStates: List[Int] = (0 to 100).toList

  override def nonTerminalStates: List[Int] = (1 to 99).toList

  override def stateProbabilities(s: Int, a: Int): Map[Int, Probability] = Map(s + a -> probability_head, s - a -> (1 - probability_head))

  override def reward(oldState: Int, newState: Int, a: Int): Reward = if (newState == 100) 1 else 0

  override def possibleActions(state: Int): Set[Int] = (0 to math.min(state, 100 - state)).toSet
}
