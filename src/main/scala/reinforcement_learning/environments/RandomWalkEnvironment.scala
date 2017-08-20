package reinforcement_learning.environments

import reinforcement_learning.{Environment, Reward}

object RandomWalkAction extends Enumeration {
  val Left, Right = Value
}

import RandomWalkAction._


case class RandomWalkEnvironment() extends Environment[Int, RandomWalkAction.Value] {
  var state: Int = _

  override def initialState: Int = {
    state = 3
    state
  }

  override def nextState(a: RandomWalkAction.Value): (Int, Boolean, Reward) = {
    state += (a match {
      case Left => -1
      case Right => 1
    })
    (state, List(0, 6).contains(state), if (state == 6) 1 else 0)
  }

  override def possibleActions(state: Int): Set[RandomWalkAction.Value] = RandomWalkAction.values
}
