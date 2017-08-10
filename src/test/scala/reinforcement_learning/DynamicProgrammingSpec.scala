package reinforcement_learning

import org.scalatest.{FlatSpec, Matchers}
import reinforcement_learning.games.{Action, GridWorldGame}

class DynamicProgrammingSpec extends FlatSpec with Matchers {
  "DynamicProgramming" should "execute iterativePolicyEvaluation correctly" in {
    val game = GridWorldGame(4, 4)
    val values = DynamicProgramming.iterativePolicyEvaluation[Int, Action.Value](
      game,
      _ => Action.values.map(_ -> 1.0 / Action.values.size).toMap,
      1.0,
      0.01
    )
    val expected = List(0, -14, -20, -22, -14, -18, -20, -20, -20, -20, -18, -14, -22, -20, -14, 0)
    values.toList.sortBy(_._1).map(_._2).map(math.round) should be(expected)
  }
}
