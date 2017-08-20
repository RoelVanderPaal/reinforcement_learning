package reinforcement_learning

import org.scalatest.{FlatSpec, Matchers}
import reinforcement_learning.environments.BlackJackAction._
import reinforcement_learning.environments.{BlackJackEnvironment, BlackJackState}
import reinforcement_learning.known_environments.{GridWorldAction, GridWorldEnvironment}

import scala.util.Random

class MonteCarloSpec extends FlatSpec with Matchers {
  "MonteCarlo" should "execute a first-visit state value estimation for blackjack" in {
    val size = 10
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


  it should "execute first-visit state value estimation for gridworld" in {
    val gridWorld = GridWorldEnvironment(4, 4)
    val knownEnvironment = KnownEnvironmentWrapper(gridWorld)
    val values = MonteCarlo.firstVisitStateValueEstimation(knownEnvironment, (s: Int) => {
      val actions = gridWorld.possibleActions(s)
      actions.toVector(Random.nextInt(actions.size))
    }, 1.0, 100)
    val expected = List(-14, -20, -22, -14, -18, -20, -20, -20, -20, -18, -14, -22, -20, -14)
//    values.toList.sortBy(_._1).map(_._2).map(math.round) should be(expected)
  }

  it should "execute ES" in {
    val environment = BlackJackEnvironment()
    val (policy, _) = MonteCarlo.control(environment, 10)

    //    for (g <- policy.groupBy { case (BlackJackState(_, v, _), _) => v }) {
    //      val (usableAce, m) = g
    //      println(usableAce)
    //      for (groupedByCurrentSum <- m.groupBy { case (BlackJackState(c, _, _), _) => c }.toSeq.sortBy(_._1).reverse) {
    //        val sortedByDealersHand = groupedByCurrentSum._2.toSeq.sortBy { case (BlackJackState(_, _, d), _) => d }
    //        val actionsString = sortedByDealersHand.map(_._2).map(_ match {
    //          case Stick => "s"
    //          case Hit => " "
    //        }
    //        ).mkString(" ")
    //        println(s"${groupedByCurrentSum._1}: $actionsString")
    //      }
    //    }
  }
}
