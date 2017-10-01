package reinforcement_learning.environments

import reinforcement_learning.{Environment, Reward}
import reinforcement_learning.known_environments.GridWorldAction
import GridWorldAction._

class CliffWalkEnvironment(rowSize: Int, colSize: Int) extends Environment[Int, GridWorldAction.Value] {
  private var s: Int = _
  private val size = rowSize * colSize
  private val start = (colSize - 1) * rowSize
  private val end = colSize * rowSize - 1
  private val cliff = (start + 1 until end).toList

  override def initialState: Int = (colSize - 1) * rowSize

  override def nextState(a: GridWorldAction.Value): (Int, Boolean, Reward) = {
    s = a match {
      case Up => if (s - rowSize < 0) s else s - rowSize
      case Down => if (s + rowSize > size - 1) s else s + rowSize
      case Right => if ((s + 1) % rowSize < s % rowSize) s else s + 1
      case Left => if (s == 0 || (s - 1) % rowSize > s % rowSize) s else s - 1
    }
    val in_cliff = cliff.contains(s)
    if (in_cliff) {
      s = start
    }
    (s, s == end, if (in_cliff) -100.0 else -1.0)
  }

  override def possibleActions(state: Int): Set[GridWorldAction.Value] = GridWorldAction.values
}
