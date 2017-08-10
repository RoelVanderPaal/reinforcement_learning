package reinforcement_learning.environments

import org.scalatest.{FlatSpec, Matchers}

class GridWorldEnvironmentSpec extends FlatSpec with Matchers {
  "A GridWorldEnvironment" should "have the right states" in {
    GridWorldEnvironment(3, 4).allStates should equal(List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11))
  }
  it should "return the right reward" in {
    val environment = GridWorldEnvironment(3, 4)
    val states = environment.allStates
    for (s1 <- states; s2 <- states; a <- environment.actions) {
      environment.reward(s1, s2, a) should equal(-1)
    }
  }
  it should "return the right state probabilities" in {
    val environment = GridWorldEnvironment(3, 4)

    def check(result: List[Int], action: Action.Value): Any = {
      environment.allStates.map(s => environment.stateProbabilities(s, action)).flatMap(_.keys) should equal(result)
    }

    check(List(0, 1, 2, 0, 1, 2, 3, 4, 5, 6, 7, 11), Action.Up)
    check(List(0, 4, 5, 6, 7, 8, 9, 10, 11, 9, 10, 11), Action.Down)
    check(List(0, 2, 2, 4, 5, 5, 7, 8, 8, 10, 11, 11), Action.Right)
    check(List(0, 0, 1, 3, 3, 4, 6, 6, 7, 9, 9, 11), Action.Left)
  }
}
