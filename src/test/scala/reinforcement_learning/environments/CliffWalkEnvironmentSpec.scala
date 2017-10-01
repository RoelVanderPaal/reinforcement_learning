package reinforcement_learning.environments

import org.scalatest.{FlatSpec, Matchers}

class CliffWalkEnvironmentSpec extends FlatSpec with Matchers {
  "A CliffWalkEnvironment" should "have correct initialState" in {
    val environment = new CliffWalkEnvironment(12, 4)
    environment.initialState should be(36)
  }
}
