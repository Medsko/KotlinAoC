package aoc2019.solutions

import org.junit.Test
import util.FileConstants
import java.io.File
import kotlin.test.assertEquals

class Day01FuelRequirements {

	@Test
	fun challengeA() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay1.txt"
		var totalFuelRequired = 0
		File(inputFile).forEachLine {
			val mass = Integer.parseInt(it)
			totalFuelRequired += (mass / 3 - 2)
		}
		println("Total fuel required for launch: $totalFuelRequired")
		assertEquals(3394106, totalFuelRequired)
	}

	@Test
	fun challengeB() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay1.txt"
		var totalFuelRequired = 0
		File(inputFile).forEachLine {
			val mass = Integer.parseInt(it)
			totalFuelRequired += calculateFuelRequired(mass)
		}
		println("Total fuel required for launch, accounting for mass of fuel: $totalFuelRequired")
		assertEquals(5088280, totalFuelRequired)
	}

	private fun calculateFuelRequired(mass: Int): Int {
		// Calculate the amount of fuel needed for this mass.
		var requiredFuel = mass / 3 - 2
		// A negative amount of fuel should be interpreted as no required fuel.
		if (requiredFuel < 0) requiredFuel = 0
		// If the amount of fuel requires more fuel to account for its mass, add it now.
		if (requiredFuel != 0) requiredFuel += calculateFuelRequired(requiredFuel)

		return requiredFuel
	}
}