package reinforcement_learning.known_environments

import org.scalatest.{FlatSpec, Matchers}
import reinforcement_learning.environments.WindyGridWorldEnvironment

import GridWorldAction._

class WindyGridWorldEnvironmentSpec extends FlatSpec with Matchers {
  "A WindyGridWorld" should "work corectly" in {
    val environment = new WindyGridWorldEnvironment(5, List(0, 0, 0, 1, 1, 1, 2, 2, 1, 0), 30, 37)
    environment.initialState should be(30)

    val expected = List(31, 32, 33, 24, 15, 6, 7, 8)
    expected.foreach(e => {
      val tuple = environment.nextState(Right)
      tuple should be((e, false, -1))
    }
    )
  }

}
