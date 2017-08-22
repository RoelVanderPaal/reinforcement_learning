package reinforcement_learning

import java.io.{BufferedWriter, File, FileWriter}

import org.scalatest.{FlatSpec, Matchers}
import reinforcement_learning.environments.BlackJackAction.{Hit, Stick}
import reinforcement_learning.environments._
import reinforcement_learning.known_environments.GridWorldEnvironment
import reinforcement_learning.util.RandomUtilImpl

class TemporalDifferenceSpec extends FlatSpec with Matchers {
  "TemporalDifference" should "execute a TD0 state value estimation for blackjack" in {
    val size = 10

    val policy = (s: BlackJackState) => if (s.currentSum >= 20) Stick else Hit
    val environment = BlackJackEnvironment()
    val results = TemporalDifference.tabularTD0(environment, policy, 1.0, .9, size)

    //    for (usableAce <- List(true, false)) {
    //      val file = new File(s"target/TD_BlackJack_${size}_$usableAce.txt")
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
    //    }
  }
  it should "execute TD0 on random walk" in {
    val environment = RandomWalkEnvironment()
    val policy = (s: Int) => new RandomUtilImpl().selectRandom(environment.possibleActions(s))
    val results = TemporalDifference.tabularTD0(environment, policy, 1.0, .1, 100, Some((1 to 5).map(i => i -> .5)))
    //    println(results)
  }
  it should "execute SARSA on blackjack" in {
    val environment = BlackJackEnvironment()
    val policy = TemporalDifference.SARSA(environment, 0.1, 1.0, 0.1, 5000000)

    for (g <- policy.groupBy { case (BlackJackState(_, v, _), _) => v }) {
      val (usableAce, m) = g
      println(usableAce)
      for (groupedByCurrentSum <- m.groupBy { case (BlackJackState(c, _, _), _) => c }.toSeq.sortBy(_._1).reverse) {
        val sortedByDealersHand = groupedByCurrentSum._2.toSeq.sortBy { case (BlackJackState(_, _, d), _) => d }
        val actionsString = sortedByDealersHand.map(_._2).map {
          case Stick => "s"
          case Hit => " "
        }.mkString(" ")
        println(s"${groupedByCurrentSum._1}: $actionsString")
      }
    }
  }
  it should "execute Q-learning on blackjack" in {

    val environment = BlackJackEnvironment()
    //    implicit val randomUtilImpl = new FixedRandomUtilImpl(List(0))
    val policy = TemporalDifference.QLearning(environment, 0.1, 1.0, 0.1, 5000000)


    for (g <- policy.groupBy { case (BlackJackState(_, v, _), _) => v }) {
      val (usableAce, m) = g
      println(usableAce)
      for (groupedByCurrentSum <- m.groupBy { case (BlackJackState(c, _, _), _) => c }.toSeq.sortBy(_._1).reverse) {
        val sortedByDealersHand = groupedByCurrentSum._2.toSeq.sortBy { case (BlackJackState(_, _, d), _) => d }
        val actionsString = sortedByDealersHand.map(_._2).map {
          case Stick => "s"
          case Hit => " "
        }.mkString(" ")
        println(s"${groupedByCurrentSum._1}: $actionsString")
      }
    }
  }
  it should "execute SARSA on gridworld" in {
    val rows = 5
    val cols = 5
    val environment = KnownEnvironmentWrapper(GridWorldEnvironment(rows, cols))
    val policy = TemporalDifference.SARSA(environment, 0.1, 1.0, 0.5, 10)

    //    val str = (0 until rows).map { r => (0 until cols).map(c => r * cols + c).map(policy.getOrElse(_, "")).mkString("\t") }.mkString("\n")
    //    println(str)
  }
  it should "execute SARSA on windy gridworld" in {
    val rows = 7
    val wind = List(0, 0, 0, 1, 1, 1, 2, 2, 1, 0)
    val cols = wind.length
    val environment = new WindyGridWorldEnvironment(rows, wind, 30, 37)
    val policy = TemporalDifference.SARSA(environment, 0.1, 1.0, 0.5, 10)

    //    val str = (0 until rows).map { r => (0 until cols).map(c => r * cols + c).map(policy.getOrElse(_, "")).mkString("\t") }.mkString("\n")
    //    println(str)
  }
}
