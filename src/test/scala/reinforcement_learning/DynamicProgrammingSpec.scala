package reinforcement_learning

import org.scalatest.{FlatSpec, Matchers}
import reinforcement_learning.known_environments.{GamblersProblemEnvironment, GridWorldAction, GridWorldEnvironment}


class DynamicProgrammingSpec extends FlatSpec with Matchers {
  "DynamicProgramming" should "execute iterativePolicyEvaluation correctly for GridWorldEnvironment" in {
    val environment = GridWorldEnvironment(4, 4)
    val policy = (_: Int) => GridWorldAction.values.map(_ -> 1.0 / GridWorldAction.values.size).toMap
    val values = DynamicProgramming.iterativePolicyEvaluation(environment, policy, 1.0, 0.01)
    val expected = List(0, -14, -20, -22, -14, -18, -20, -20, -20, -20, -18, -14, -22, -20, -14, 0)
    values.toList.sortBy(_._1).map(_._2).map(math.round) should be(expected)
  }
  it should "execute valueIteration correctly for GamblersProblem" in {
    val environment = new GamblersProblemEnvironment(0.4)
    val values = DynamicProgramming.valueIteration(environment, 1.0, 0.01)
  }
}
