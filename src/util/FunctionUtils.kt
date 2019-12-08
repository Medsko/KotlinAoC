package util

import kotlin.math.abs

object FunctionUtils {
	val hash = 35
	val period = 46
	val star = 42

	fun manhattanDistance(start: Pair<Int, Int>, destination: Pair<Int, Int>): Int {
		return abs(start.first - destination.first) + abs(start.second - destination.second)
	}

	fun absoluteDifference(first: Int, second: Int): Int {
		return abs(abs(first) - abs(second))
	}
}