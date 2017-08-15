package reinforcement_learning

import org.scalatest.FlatSpec
import reinforcement_learning.environments.BlackJackAction._
import reinforcement_learning.environments.{BlackJackEnvironment, BlackJackState}

class MonteCarloSpec extends FlatSpec {
  "MonteCarlo" should "execute a first-visit state value estimation" in {
    val size = 500000
    //    implicit val intGenerator = new FixedIntGenerator(List(1, 9, 2, 10, 10, 1, 9, 2, 10, 9).map(_ - 1))
    val policy = (s: BlackJackState) => if (s.currentSum >= 20) Stick else Hit
    val environment = BlackJackEnvironment()
    val results = MonteCarlo.firstVisitStateValueEstimation(environment, policy, 1.0, size)

    //    for (usableAce <- List(true, false)) {
    //      val file = new File(s"target/MC_BlackJack_${size}_$usableAce.txt")
    //      val bw = new BufferedWriter(new FileWriter(file))
    //      val dealerShowingCards = 1 to 10
    //      bw.write(dealerShowingCards.mkString(",") + "\n")
    //      val line = (12 to 21).map(currentSum =>
    //        dealerShowingCards.map(dealerShowingCard => {
    //          val state = BlackJackState(currentSum, usableAce, dealerShowingCard)
    //          results.getOrElse(state, 0.0)
    //        })
    //          .mkString(","))
    //        .mkString("\n")
    //      bw.write(line)
    //      bw.close()
    //
    //    }
  }
}
