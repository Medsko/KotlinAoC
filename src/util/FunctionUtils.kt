package util

import java.util.*
import kotlin.math.abs
import kotlin.math.atan2

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

	/**
	 * Finds all different orders of the given collection and returns them as a list of lists.
	 */
	fun <T> permutations(collection: Collection<T>, current: Stack<T>, permutations: MutableList<List<T>> = ArrayList()): List<List<T>> {

		val availableItems = ArrayList(collection)

		for (item in collection) {

			current.push(item)
			availableItems.remove(item)

			permutations(availableItems, current, permutations)

			permutations.add(ArrayList(current))

			availableItems.add(current.pop())
		}
		// Filter out all incomplete permutations.
		val maxSize = permutations.maxBy { it.size }?.size
		return permutations.filter { it.size == maxSize }
	}

	fun angleBetweenPoints(start: Pair<Int, Int>, target: Pair<Int, Int>, invertedY: Boolean): Double {
		val yDelta = if (invertedY) start.second - target.second else target.second - start.second
		val xDelta = target.first - start.first
		val angle = Math.toDegrees(atan2(xDelta.toDouble(), yDelta.toDouble()))
		return if (angle < 0) 360 + angle else angle
	}

}