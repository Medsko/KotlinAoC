package aoc2018.dao

import util.FunctionUtils

data class Star(val id: Int, var position: Pair<Int, Int>, val velocity: Pair<Int, Int>) {

	fun isNeighbor(other: Star): Boolean {
		if (other.id == id) return false // A star is not its own neighbor.
		return FunctionUtils.absoluteDifference(position.first, other.position.first) <= 1
				&& FunctionUtils.absoluteDifference(position.second, other.position.second) <= 1
	}
}
