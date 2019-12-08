package aoc2018.solutions

import aoc2018.dao.IntMatrix
import aoc2018.dao.Star
import util.FileConstants
import java.io.File

fun main() {

	val constellation = readInput()
	var seconds = 0

	while (!isCoherentConstellation(constellation)) {
		// Move all stars by their velocity.
		constellation.forEach { it.position = it.position + it.velocity }
		seconds++
		println("Iteration $seconds")
	}
	// Message: GEJKHGHZ appears after 10681 seconds.
	printConstellation(constellation)
}

fun printConstellation(constellation: List<Star>) {
	// Plot the stars on a matrix and print the result to file.
	val plottedConstellation = IntMatrix()
	for (star in constellation) {
		// Set a # for each star in the constellation.
		plottedConstellation.set(star.position, 35)
	}

	for (row in plottedConstellation.matrix) {
		if (row.all { it == 0 }) continue
		val line = row.map { it.toChar() }.toCharArray()
		println(line)
	}
}

fun isCoherentConstellation(constellation: List<Star>): Boolean {
	for (star in constellation) {
		if (constellation.none { star.isNeighbor(it) } ) {
			// This star has no neighbor. The constellation is not perfectly coherent yet.
			return false
		}
	}
	return true
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> =
	Pair(this.first + other.first, this.second + other.second)

private fun readInput(): List<Star> {
	val inputFile = FileConstants.INPUT_DIR_2018 + "inputDay10.txt"
	val initialConstellation = ArrayList<Star>()
	var starCounter = 0

	File(inputFile).forEachLine { line ->
		val pairRegex = "<.+?>".toRegex()
		val pairs = pairRegex.findAll(line)
		val initialPosition = refineInput(pairs.elementAt(0))
		val velocity = refineInput(pairs.elementAt(1))
		val newStar = Star(starCounter++, initialPosition, velocity)
		initialConstellation.add(newStar)
	}

	return initialConstellation
}

fun refineInput(input: MatchResult): Pair<Int, Int> {
	val values = input.value.replace("[<>]".toRegex(), "").split(",")
	val first = Integer.parseInt(values[0].trim())
	val second = Integer.parseInt(values[1].trim())
	return Pair(first, second)
}