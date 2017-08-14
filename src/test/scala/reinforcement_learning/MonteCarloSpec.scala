package reinforcement_learning

import org.scalatest.FlatSpec
import reinforcement_learning.environments.BlackJackAction._
import reinforcement_learning.environments.{BlackJackEnvironment, BlackJackState}

class MonteCarloSpec extends FlatSpec {


  "MonteCarlo" should "execute a first-visit state value estimation" in {
    val policy = (s: BlackJackState) => if (s.currentSum >= 20) Stick else Hit
    val environment = BlackJackEnvironment()
    val result = MonteCarlo.firstVisitStateValueEstimation(environment, policy, 1.0, 10)
    println(result)
  }
}
