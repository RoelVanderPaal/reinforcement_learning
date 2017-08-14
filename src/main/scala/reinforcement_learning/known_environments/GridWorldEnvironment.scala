package reinforcement_learning.known_environments

import reinforcement_learning.KnownEnvironment

object GridWorldAction extends Enumeration {
  val Up, Down, Left, Right = Value
}


import reinforcement_learning.known_environments.GridWorldAction._

case class GridWorldEnvironment(rowSize: Int, colSize: Int) extends KnownEnvironment[Int, GridWorldAction.Value] {
  val size: Int = rowSize * colSize

  val terminalStates = List(0, size - 1)

  override def allStates: List[Int] = (0 until size).toList

  override def nonTerminalStates: List[Int] = allStates diff terminalStates

  override def stateProbabilities(s: Int, a: GridWorldAction.Value): Map[Int, Double] = {
    val i = if (terminalStates.contains(s))
      s else
      a match {
        case Up => if (s - rowSize < 0) s else s - rowSize
        case Down => if (s + rowSize > size - 1) s else s + rowSize
        case Right => if ((s + 1) % rowSize < s % rowSize) s else s + 1
        case Left => if ((s - 1) % rowSize > s % rowSize) s else s - 1
      }
    Map(i -> 1.0)
  }

  override def reward(oldState: Int, newState: Int, a: GridWorldAction.Value): Double = -1

  override def possibleActions(state: Int) = GridWorldAction.values.toList
}
