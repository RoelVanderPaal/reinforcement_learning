package reinforcement_learning.environments

import reinforcement_learning.{Environment, Reward}
import reinforcement_learning.known_environments.GridWorldAction

import GridWorldAction._

class WindyGridWorldEnvironment(colSize: Int, wind: Seq[Int], start: Int, end: Int) extends Environment[Int, GridWorldAction.Value] {
  private var s: Int = _

  private val rowSize = wind.length
  private val size: Int = rowSize * colSize


  override def initialState: Int = {
    s = start
    s
  }

  override def nextState(a: GridWorldAction.Value): (Int, Boolean, Reward) = {

    def up = {
      if (s - rowSize < 0) s else s - rowSize
    }

    val nextStateBeforeWind = a match {
      case Up => up
      case Down => if (s + rowSize > size - 1) s else s + rowSize
      case Right => if ((s + 1) % rowSize < s % rowSize) s else s + 1
      case Left => if (s == 0 || (s - 1) % rowSize > s % rowSize) s else s - 1
    }

    val i = wind(s % rowSize)
    s = nextStateBeforeWind
    (0 until i).foreach(_ => {
      s = up
    })

    (s, s == end, -1)
  }

  override def possibleActions(state: Int): Set[GridWorldAction.Value] = GridWorldAction.values
}
