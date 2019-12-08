package aoc2019.solutions

import util.FileConstants
import util.FunctionUtils
import java.io.File
import java.util.*
import kotlin.collections.HashMap

fun main() {
	// Challenge A
	val crossings = determineIntersections()
	val closestCrossing = crossings.keys.minBy { FunctionUtils.manhattanDistance(Pair(0, 0), it) }
	if (closestCrossing != null) {
		println("Manhattan distance to closest intersection: ${FunctionUtils.manhattanDistance(Pair(0, 0), closestCrossing)}")
	}
	// Answer: 1337

	val fewestCombinedSteps = crossings.values.min()
	println("Fewest combined steps to get to an intersection: $fewestCombinedSteps")

}

fun determineIntersections(): HashMap<Pair<Int, Int>, Int> {
	val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay3.txt"
	val wires = ArrayList<List<String>>()
	File(inputFile).forEachLine {
		wires.add(it.split(","))
	}

	val wirePathOne = buildWirePath(wires[0])
	val wirePathTwo = buildWirePath(wires[1])

	val crossings = HashMap<Pair<Int, Int>, Int>()
	// TODO: optimize the crap out of this shit
	// Compose a list of all positions that both wires pass.
	for ((firstSteps, position) in wirePathOne.reversed().withIndex()) {
		for ((secondSteps, otherPosition) in wirePathTwo.reversed().withIndex()) {
			// Only the first time both wires encounter each other counts for challenge B.
			if (!crossings.contains(position) && position == otherPosition) {
				crossings[position] = firstSteps + secondSteps + 2 // Account for zero-based index vs one-based steps.
			}
		}
	}

	return crossings
}

fun buildWirePath(instructions: List<String>): Deque<Pair<Int, Int>> {
	val wirePath = ArrayDeque<Pair<Int, Int>>()
	// Add the starting point.
	wirePath.add(Pair(0, 0))
	// Carry out the instructions.
	instructions.forEach {
		move(it, wirePath)
	}
	// Remove the starting point, as it is shared among both wires.
	wirePath.removeLast()
	return wirePath
}

fun move(instruction: String, path: Deque<Pair<Int, Int>>) {
	val distance = Integer.parseInt(instruction.substring(1))
	val direction = determineDirection(instruction.substring(0, 1))

	for (i in 1..distance) {
		val previousPosition = path.peek()
		val nextPosition = previousPosition + direction
		path.push(nextPosition)
	}
}

fun determineDirection(direction: String): Pair<Int, Int> {
	when (direction) {
		"R" -> {
			return Pair(1, 0)
		}
		"U" -> {
			return Pair(0, -1)
		}
		"L" -> {
			return Pair(-1, 0)
		}
		"D" -> {
			return Pair(0, 1)
		}
	}
	return Pair(0, 0)
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> =
	Pair(this.first + other.first, this.second + other.second)
