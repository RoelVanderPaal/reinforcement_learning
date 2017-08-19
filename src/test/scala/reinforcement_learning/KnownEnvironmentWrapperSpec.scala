package reinforcement_learning

import org.scalatest.{FlatSpec, Matchers}
import reinforcement_learning.known_environments.GridWorldEnvironment

class KnownEnvironmentWrapperSpec extends FlatSpec with Matchers {
  private val gridWorld = GridWorldEnvironment(4, 4)
  "A KnownEnvironmentWrapper" should "return initialState" in {
    val knownEnvironment = KnownEnvironmentWrapper(gridWorld)
    gridWorld.nonTerminalStates should contain(knownEnvironment.initialState)
  }
  it should "return a next state" in {
    val knownEnvironment = KnownEnvironmentWrapper(gridWorld)
    val state = knownEnvironment.initialState
    val actions = knownEnvironment.possibleActions(state)
    val (nextState, done, _) = knownEnvironment.nextState(actions.head)
    gridWorld.allStates should contain(nextState)
    gridWorld.nonTerminalStates.contains(nextState) should be(!done)
  }

}
