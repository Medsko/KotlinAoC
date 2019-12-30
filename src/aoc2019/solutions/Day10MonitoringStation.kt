package aoc2019.solutions

import org.junit.Test
import util.FileConstants
import util.FunctionUtils
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.test.assertEquals

class Day10MonitoringStation {

	@Test
	fun challengeB() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay10.txt"
		val asteroids = File(inputFile).readLines().map { it.toCharArray() }
		val monitoringStation = Pair(11, 13)
		val vaporizedAsteroids = vaporizeAsteroids(asteroids, monitoringStation)
		val asteroidToBetOn = vaporizedAsteroids[199]
		println("The 200th asteroid to be vaporized is: $asteroidToBetOn")
		val checksum = asteroidToBetOn.first * 100 + asteroidToBetOn.second
		assertEquals(604, checksum)
	}

	@Test
	fun testChallengeB() {
		val inputSet = prepareInputSets()[3]
		val testeroids = inputSet.first
		val testMonitoringStation = inputSet.second
		val vaporizedAsteroids = vaporizeAsteroids(testeroids, testMonitoringStation)
		// The 1st asteroid to be vaporized is at 11,12.
		assertEquals(Pair(11, 12), vaporizedAsteroids[0])
		// The 50th asteroid to be vaporized is at 16,9.
		assertEquals(Pair(16, 9), vaporizedAsteroids[49])
		// The 200th asteroid to be vaporized is at 8,2.
		assertEquals(Pair(8, 2), vaporizedAsteroids[199])
		// The 299th and final asteroid to be vaporized is at 11,1.
		assertEquals(Pair(11, 1), vaporizedAsteroids[vaporizedAsteroids.size - 1])
	}

	@Test
	fun challengeA() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay10.txt"
		val asteroids = File(inputFile).readLines().map { it.toCharArray() }
		val output = determineOptimalMonitoringStationLocation(asteroids)
		println("Asteroid at ${output.first} has the best view, with ${output.second} asteroids visible.")
		assertEquals(Pair(11, 13), output.first)
		assertEquals(227, output.second)
	}

	@Test
	fun testChallengeA() {
		val inputSets = prepareInputSets()
		for (inputSet in inputSets) {
			val output = determineOptimalMonitoringStationLocation(inputSet.first)
			assertEquals(inputSet.second, output.first)
			assertEquals(inputSet.third, output.second)
		}
	}

	private fun vaporizeAsteroids(asteroids: List<CharArray>, monitoringStation: Pair<Int, Int>): ArrayList<Pair<Int, Int>> {
		val asteroidsPerAngle = TreeMap<Double, MutableList<Pair<Int, Int>>>()
		countVisibleAsteroids(asteroids, monitoringStation, asteroidsPerAngle)
		// Sort all lists by Manhattan distance to monitoring station, so closest asteroid in angle is vaporized first.
		asteroidsPerAngle.values.forEach { list -> list.sortBy { FunctionUtils.manhattanDistance(monitoringStation, it) } }

		val vaporizedAsteroids = ArrayList<Pair<Int, Int>>()
		var count = 0
		val nrOfAsteroids = asteroidsPerAngle.values.fold(0) { acc, list -> acc + list.size }
		while (count < nrOfAsteroids) {
			for (angle in asteroidsPerAngle.keys) {
				val asteroidsInAngle = asteroidsPerAngle[angle]
				if (asteroidsInAngle == null || asteroidsInAngle.isEmpty()) continue
				// There are still asteroids in this angle. Destroy the next one.
				val vaporizedAsteroid = asteroidsInAngle[0]
				vaporizedAsteroids.add(vaporizedAsteroid)
				asteroidsInAngle.remove(vaporizedAsteroid)
				count++
			}
		}
		return vaporizedAsteroids
	}

	private fun determineOptimalMonitoringStationLocation(asteroids: List<CharArray>): Pair<Pair<Int, Int>, Int> {
		val visibilityCountPerAsteroid = HashMap<Pair<Int, Int>, Int>()
		for (yIndex in asteroids.indices) {
			for (xIndex in asteroids.indices) {
				if (asteroids[yIndex][xIndex] == '.') continue	// Skip squares with no asteroid.
				val currentAsteroid = Pair(xIndex, yIndex)
				visibilityCountPerAsteroid[currentAsteroid] = countVisibleAsteroids(asteroids, currentAsteroid)
			}
		}
		val optimalAsteroid = visibilityCountPerAsteroid.entries.maxBy { it.value } ?: return Pair(Pair(-1, -1), -1)
		return Pair(optimalAsteroid.key, optimalAsteroid.value)
	}

	private fun countVisibleAsteroids(asteroids: List<CharArray>, currentAsteroid: Pair<Int, Int>,
									  asteroidsPerAngle: MutableMap<Double, MutableList<Pair<Int, Int>>> = HashMap()): Int {
		var count = 0

		for (yIndex in asteroids.indices) {
			for (xIndex in asteroids.indices) {
				if (asteroids[yIndex][xIndex] == '.') continue	// Skip squares with no asteroid.
				val otherAsteroid = Pair(xIndex, yIndex)
				if (otherAsteroid == currentAsteroid) continue	// Skip the asteroid itself.
				val angle = FunctionUtils.angleBetweenPoints(currentAsteroid, otherAsteroid, true)
				// Add the other asteroid to the list of asteroids on this angle.
				val asteroidsOnAngle = asteroidsPerAngle.computeIfAbsent(angle) {
					ArrayList()
				}
				asteroidsOnAngle.add(otherAsteroid)
				if (asteroidsOnAngle.size > 1) continue	// Not the first asteroid on this angle.
				count++
			}
		}
		return count
	}

	private fun prepareInputSets(): ArrayList<Triple<List<CharArray>, Pair<Int, Int>, Int>> {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay10Test.txt"
		val inputLines = File(inputFile).readLines()
		var currentAsteroids = ArrayList<String>()
		val inputSets = ArrayList<Triple<List<CharArray>, Pair<Int, Int>, Int>>()

		for (line in inputLines) {
			if (line.startsWith("---")) {
				continue
			}
			if (line.matches("\\d+,\\d+,\\d+".toRegex())) {
				val targetOutput = line.split(",")
				val targetPosition = Pair(targetOutput[0].toInt(), targetOutput[1].toInt())
				val targetVisibleAsteroids = targetOutput[2].toInt()
				val separatedAsteroids = currentAsteroids.map { it.toCharArray() }
				val inputSet = Triple(separatedAsteroids, targetPosition, targetVisibleAsteroids)
				inputSets.add(inputSet)
				currentAsteroids = ArrayList()
			} else {
				currentAsteroids.add(line)
			}
		}
		return inputSets
	}
}
