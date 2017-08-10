package reinforcement_learning.games

import reinforcement_learning.Game

object Action extends Enumeration {
  val Up, Down, Left, Right = Value
}

case class GridWorldGame(rowSize: Int, colSize: Int) extends Game[Int, Action.Value] {
  val actions = List(Action.Up, Action.Down, Action.Left, Action.Right)

  val size = rowSize * colSize

  val terminalStates = List(0, size - 1)

  override def allStates = (0 until size).toList

  override def nonTerminalStates = (1 until size - 1).toList

  override def stateProbabilities(s: Int, a: Action.Value) = {
    val i = if (terminalStates.contains(s))
      s else
      a match {
        case Action.Up => if (s - rowSize < 0) s else s - rowSize
        case Action.Down => if (s + rowSize > size - 1) s else s + rowSize
        case Action.Right => if ((s + 1) % rowSize < s % rowSize) s else s + 1
        case Action.Left => if ((s - 1) % rowSize > s % rowSize) s else s - 1
      }
    Map(i -> 1.0)
  }

  override def reward(oldState: Int, newState: Int, a: Action.Value) = -1

}
