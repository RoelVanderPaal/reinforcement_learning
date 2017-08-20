package reinforcement_learning.environments

import reinforcement_learning.util.{RandomUtil, RandomUtilImpl}
import reinforcement_learning.{Environment, Reward}

import scala.collection.mutable.ListBuffer

case class BlackJackState(currentSum: Int, useableAce: Boolean, dealerShowingCard: Int)

object BlackJackAction extends Enumeration {
  val Hit, Stick = Value
}

import BlackJackAction._

case class BlackJackEnvironment()(implicit randomGenerator: RandomUtil = new RandomUtilImpl()) extends Environment[BlackJackState, BlackJackAction.Value] {
  type Hand = Traversable[Int]
  private var player = ListBuffer[Int]()
  private var dealer = ListBuffer[Int]()

  override def possibleActions(state: BlackJackState): Set[BlackJackAction.Value] = BlackJackAction.values

  override def initialState: BlackJackState = {
    player.clear()
    dealer.clear()
    while (sum(player) < 12) {
      player += drawCard
    }
    dealer += drawCard
    getState
  }

  override def nextState(action: BlackJackAction.Value): (BlackJackState, Boolean, Reward) = {
    val (done, reward) = action match {
      case Hit =>
        player += drawCard
        if (sum(player) > 21)
          (true, -1)
        else
          (false, 0)
      case Stick =>
        while (sum(dealer) < 17) {
          dealer += drawCard
        }
        (true, score(player).compare(score(dealer)))
    }
    (getState, done, reward)
  }

  private def drawCard = {
    randomGenerator.nextInt(13) + 1 match {
      case x if x > 10 => 10
      case x => x
    }
  }

  private def getState = BlackJackState(sum(player), usableAce(player), dealer.head)

  private def usableAce(hand: Hand) = hand.exists(_ == 1) && hand.sum + 10 <= 21

  private def sum(hand: Hand) = hand.sum + (if (usableAce(hand)) 10 else 0)

  private def score(hand: Hand) = if (sum(hand) > 21) 0 else sum(hand)

}
