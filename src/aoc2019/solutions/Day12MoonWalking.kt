package aoc2019.solutions

import aoc2019.dao.Moon
import org.junit.Test
import util.FunctionUtils
import kotlin.math.abs
import kotlin.test.assertEquals

class Day12MoonWalking {

	@Test
	fun challengeB() {
		val moons = prepareMoons()
		val stepsUntilStateDejaVu = determineStepsUntilStateDejaVu(moons)
		assertEquals(354540398381256, stepsUntilStateDejaVu)
	}

	@Test
	fun testChallengeB() {
		val moons = prepareTestMoons()
		val stepsUntilStateDejaVu = determineStepsUntilStateDejaVu(moons)
		assertEquals(2772, stepsUntilStateDejaVu)
	}

	private fun determineStepsUntilStateDejaVu(moons: List<Moon>): Long {
		val startingStates = ArrayList<List<Pair<Int, Int>>>()
		for (dimension in 0..2) {
			startingStates.add(moons.map { moon -> Pair(moon.position[dimension], moon.velocity[dimension]) })
		}
		val stepsUntilStateReoccurs = Array<Long?>(3) { null }
		var steps = 0L

		while (stepsUntilStateReoccurs.any { it == null }) {
			updateMoons(moons)
			steps++
			for (dimension in 0..2) {
				if (stepsUntilStateReoccurs[dimension] != null) continue // Already found the number of steps until state recurrence.
				val currentStateDimension = moons.map { moon -> Pair(moon.position[dimension], moon.velocity[dimension]) }
				if (startingStates[dimension] == currentStateDimension) stepsUntilStateReoccurs[dimension] = steps
			}
		}
		return stepsUntilStateReoccurs.filterNotNull().fold(1L) { x, y -> FunctionUtils.leastCommonMultiple(x, y)}
	}

	@Test
	fun challengeA() {
		val moons = prepareMoons()
		for (i in 1..1000) updateMoons(moons)
		val totalEnergy = moons.fold(0) { acc, moon -> acc + calculateTotalEnergy(moon) }
		println("Total energy in system after simulating 1000 steps: $totalEnergy")
		assertEquals(7758, totalEnergy)
	}

	@Test
	fun testChallengeA() {
		var moons = prepareTestMoons()
		for (i in 1..10) updateMoons(moons)
		var totalEnergy = moons.fold(0) { acc, moon -> acc + calculateTotalEnergy(moon) }
		assertEquals(179, totalEnergy)

		moons = prepareTestMoonsCase2()
		for (i in 1..100) updateMoons(moons)
		totalEnergy = moons.fold(0) { acc, moon -> acc + calculateTotalEnergy(moon) }
		assertEquals(1940, totalEnergy)
		for (printMoon in moons) {
			println(printMoon)
		}
	}

	private fun calculateTotalEnergy(moon: Moon): Int {
		val totalPotentialEnergy = moon.position.reduce { acc, it -> abs(acc) + abs(it) }
		val totalKineticEnergy = moon.velocity.reduce { acc, it -> abs(acc) + abs(it) }
		return totalPotentialEnergy * totalKineticEnergy
	}

	/**
	 * Moves all moons in the given list by one step.
	 */
	private fun updateMoons(moons: List<Moon>) {
		moons.forEach { moon ->
			val otherMoons = ArrayList(moons)
			otherMoons.remove(moon)
			applyGravity(moon, otherMoons)
		}
		moons.forEach { applyVelocity(it) }
	}

	private fun applyGravity(moonToUpdate: Moon, otherMoons: List<Moon>) {
		for (other in otherMoons) {
			for ((index, axis) in moonToUpdate.position.withIndex()) {
				moonToUpdate.velocity[index] += (other.position[index].compareTo(axis))
			}
		}
	}

	private fun applyVelocity(moonToUpdate: Moon) {
		moonToUpdate.position.modify(moonToUpdate.velocity)
	}

	/**
	 * Modifies the array by adding each value in the given array to the corresponding value in the current array.
	 * If the two arrays do not match in size, the array will not be modified.
	 */
	private fun Array<Int>.modify(other: Array<Int>) {
		if (this.size != other.size) return
		for (i in this.indices) this[i] += other[i]
	}

	private fun prepareTestMoons(): ArrayList<Moon> {
		val io = Moon(name = "Io", position = arrayOf(-1, 0, 2))
		val europa = Moon(name = "Europa", position = arrayOf(2, -10, -7))
		val ganymede = Moon(name = "Ganymede", position = arrayOf(4, -8, 8))
		val callisto = Moon(name = "Callisto", position = arrayOf(3, 5, -1))
		return arrayListOf(io, europa, ganymede, callisto)
	}

	private fun prepareTestMoonsCase2(): ArrayList<Moon> {
		val io = Moon(name = "Io", position = arrayOf(-8, -10, 0))
		val europa = Moon(name = "Europa", position = arrayOf(5, 5, 10))
		val ganymede = Moon(name = "Ganymede", position = arrayOf(2, -7, 3))
		val callisto = Moon(name = "Callisto", position = arrayOf(9, -8, -3))
		return arrayListOf(io, europa, ganymede, callisto)
	}

	private fun prepareMoons(): ArrayList<Moon> {
		val io = Moon(name = "Io", position = arrayOf(9, 13, -8))
		val europa = Moon(name = "Europa", position = arrayOf(-3, 16, -17))
		val ganymede = Moon(name = "Ganymede", position = arrayOf(-4, 11, -10))
		val callisto = Moon(name = "Callisto", position = arrayOf(0, -2, -2))
		return arrayListOf(io, europa, ganymede, callisto)
	}
}