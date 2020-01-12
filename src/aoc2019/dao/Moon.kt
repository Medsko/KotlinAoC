package aoc2019.dao

import java.util.*

data class Moon(val name: String = "",
				val position: Array<Int> = Array(3) {0},
				var velocity: Array<Int> = Array(3) {0}) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Moon

		if (!position.contentEquals(other.position)) return false
		if (!velocity.contentEquals(other.velocity)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = position.contentHashCode()
		result = 31 * result + velocity.contentHashCode()
		return result
	}

	override fun toString(): String {
		val coordinates = arrayOf("x", "y", "z")
		var toString = "pos=<"
		var joiner = StringJoiner(", ")
		for ((index, coordinate) in coordinates.withIndex()) {
			val leftPad = if (position[index] < 0) "" else " "
			val currentCoordinate = "$coordinate=$leftPad${position[index]}"
			joiner.add(currentCoordinate)
		}

		toString += joiner.toString()
		toString += ">, vel=<"
		joiner = StringJoiner(", ")

		for ((index, coordinate) in coordinates.withIndex()) {
			val leftPad = if (velocity[index] < 0) "" else " "
			val currentCoordinate = "$coordinate=$leftPad${velocity[index]}"
			joiner.add(currentCoordinate)
		}
		toString += joiner.toString()
		toString += ">"

		return toString
	}
}