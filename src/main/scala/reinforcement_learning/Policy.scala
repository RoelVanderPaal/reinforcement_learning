package reinforcement_learning

import reinforcement_learning.util.RandomUtil

object Policy {
  def eGreedyPolicy[Action](possibleActions: Traversable[Action], epsilon: Probability, argMaxAction: Action)(implicit randomUtil: RandomUtil): Action = {
    randomUtil.selectRandomWithProbability(possibleActions.map(a => a -> {
      if (a == argMaxAction)
        1 - epsilon + epsilon / possibleActions.size
      else
        epsilon / possibleActions.size
    }))
  }

}
