package reinforcement_learning

import org.scalatest.FlatSpec
import reinforcement_learning.environments.BlackJackAction._
import reinforcement_learning.environments.{BlackJackAction, BlackJackEnvironment, BlackJackState}

class MonteCarloSpec extends FlatSpec {
  "MonteCarlo" should "execute a first-visit state value estimation" in {
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
  it should "execute ES" in {
    val environment = BlackJackEnvironment()
    val (policy, actionValue) = MonteCarlo.ES(environment, 50000)
    println(policy)
    val policyGroups = policy.groupBy { case (BlackJackState(_, ua, _), _) => ua }
    for (g <- policyGroups) {
      val (usableAce, m) = g
      println(usableAce)
      for (s <- m.groupBy { case (BlackJackState(c, _, _), _) => c }.toSeq.sortBy(_._1).reverse) {
        val actions = s._2.toSeq.sortBy { case (BlackJackState(_, _, d), _) => d }.map(_._2)
        println(s._1 + ": " + actions.map(_ match {
          case Stick => "s"
          case Hit => " "
        }
        ).mkString(" "))
      }
    }
  }
}
