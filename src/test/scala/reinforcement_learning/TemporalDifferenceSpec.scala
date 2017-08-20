package reinforcement_learning

import java.io.{BufferedWriter, File, FileWriter}

import org.scalatest.{FlatSpec, Matchers}
import reinforcement_learning.environments.BlackJackAction.{Hit, Stick}
import reinforcement_learning.environments.{BlackJackEnvironment, BlackJackState}

class TemporalDifferenceSpec extends FlatSpec with Matchers {
  "TemporalDifference" should "execute a TD0 state value estimation for blackjack" in {
    val size = 100000

    val policy = (s: BlackJackState) => if (s.currentSum >= 20) Stick else Hit
    val environment = BlackJackEnvironment()
    val results = TemporalDifference.tabularTD0(environment, policy, 1.0, .9, size)

    for (usableAce <- List(true, false)) {
      val file = new File(s"target/TD_BlackJack_${size}_$usableAce.txt")
      val bw = new BufferedWriter(new FileWriter(file))
      val dealerShowingCards = 1 to 10
      bw.write(dealerShowingCards.mkString(",") + "\n")
      val line = (12 to 21).map(currentSum =>
        dealerShowingCards.map(dealerShowingCard => {
          val state = BlackJackState(currentSum, usableAce, dealerShowingCard)
          results.getOrElse(state, 0.0)
        })
          .mkString(","))
        .mkString("\n")
      bw.write(line)
      bw.close()

    }
  }
}
