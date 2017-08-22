package reinforcement_learning

import reinforcement_learning.util.{ArgMax, Average, RandomUtil}

import scala.collection.mutable

class StateActionValues[State, Action] {
  private val valuePerStateAndAction = mutable.Map[State, mutable.Map[Action, Reward]]()

  def value(s: State, a: Action): Double = getActionRewardMap(s).getOrElseUpdate(a, 0.0)

  private def getActionRewardMap(s: State) = valuePerStateAndAction.getOrElseUpdate(s, mutable.Map[Action, Reward]())

  def adjust(s: State, a: Action)(f: Reward => Reward): Unit = {
    val actionToReward = getActionRewardMap(s)
    actionToReward.update(a, f(actionToReward.getOrElseUpdate(a, 0.0)))
  }

  def greedyAction(s: State): Option[Action] = valuePerStateAndAction.get(s).map(ArgMax.argMax(_))

  def eGreedyAction(s: State, possibleActions: Traversable[Action], epsilon: Reward)(implicit randomUtil: RandomUtil): Action = {
    val greedyAction: Action = this.greedyAction(s).getOrElse(randomUtil.selectRandom(possibleActions))
    randomUtil.selectRandomWithProbability(possibleActions.map(a => a -> {
      if (a == greedyAction)
        1 - epsilon + epsilon / possibleActions.size
      else
        epsilon / possibleActions.size
    }))
  }

  def greedyPolicy: collection.Map[State, Action] = valuePerStateAndAction.mapValues(ArgMax.argMax(_))

  def maxReward(s: State): Double = valuePerStateAndAction.get(s).map(_.values.max).getOrElse(0.0)

  def rewards: collection.Map[State, mutable.Map[Action, Reward]] = valuePerStateAndAction
}
