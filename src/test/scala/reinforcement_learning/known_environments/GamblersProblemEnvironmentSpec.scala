package reinforcement_learning.known_environments

import org.scalatest.{FlatSpec, Matchers}

class GamblersProblemEnvironmentSpec extends FlatSpec with Matchers {
  "A GamblersProblemEnvironment" should "have correct states" in {
    val nonTerminalStates = GamblersProblemEnvironment(0.4).nonTerminalStates
    nonTerminalStates.head should be(1)
    nonTerminalStates.last should be(99)
    nonTerminalStates.size should be(99)
    val states = GamblersProblemEnvironment(0.4).allStates
    states.head should be(0)
    states.last should be(100)
    states.size should be(101)
  }
}
