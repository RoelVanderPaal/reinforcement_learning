package reinforcement_learning.environments

import reinforcement_learning.{Environment, Reward}

import scala.collection.mutable.ListBuffer

case class BlackJackState(currentSum: Int, useableAce: Boolean, dealersShowingCard: Int)

object BlackJackAction extends Enumeration {
  val Hit, Stick = Value
}

import BlackJackAction._

case class BlackJackEnvironment() extends Environment[BlackJackState, BlackJackAction.Value] {
  type Hand = Traversable[Int]
  var player = ListBuffer[Int]()
  var dealer = ListBuffer[Int]()

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

  val r = scala.util.Random

  private def drawCard = {
    (r.nextInt(13) + 1) match {
      case x if x > 10 => 10
      case x => x
    }
  }

  private def getState = BlackJackState(sum(player), usableAce(player), dealer(0))

  private def usableAce(hand: Hand) = hand.exists(_ == 1) && hand.sum + 10 <= 21

  private def sum(hand: Hand) = hand.sum + (if (usableAce(hand)) 10 else 0)

  private def score(hand: Hand) = if (sum(hand) > 21) 0 else sum(hand)

}
