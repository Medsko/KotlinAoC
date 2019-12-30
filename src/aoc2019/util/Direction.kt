package aoc2019.util

enum class Direction {
	LEFT,
	UP,
	RIGHT,
	DOWN;

	/**
	 * Determines the next position, given the current, after one move in this direction. Assumes an inverted y axis.
	 */
	fun move(currentPosition: Pair<Int, Int>): Pair<Int, Int> {
		return when(this) {
			LEFT -> Pair(currentPosition.first - 1, currentPosition.second)
			UP -> Pair(currentPosition.first, currentPosition.second - 1)
			RIGHT -> Pair(currentPosition.first + 1, currentPosition.second)
			DOWN -> Pair(currentPosition.first, currentPosition.second + 1)
		}
	}

	fun turnClockwise(): Direction {
		return when(this) {
			LEFT -> UP
			UP -> RIGHT
			RIGHT -> DOWN
			DOWN -> LEFT
		}
	}

	fun turnCounterClockwise(): Direction {
		return when(this) {
			LEFT -> DOWN
			UP -> LEFT
			RIGHT -> UP
			DOWN -> RIGHT
		}
	}
}