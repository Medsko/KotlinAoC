package aoc2019.solutions

import aoc2019.dao.Chemical
import aoc2019.util.NanoFactory
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import kotlin.collections.HashMap

const val GOAL_INDICATOR = "goal:"
const val ULTIMATE_PRODUCT = "FUEL"
const val ORE = "ORE"
const val TRILLION = 1_000_000_000_000

class Day14SpaceStoichiometry {

	companion object Judge {
		private const val ANSWER_A = 857266
		private const val ANSWER_B = 0
	}

	@Test
	fun testDay14a() {
		for (i in 1..4) {
			val chemicalFormulas = HashMap<String, Chemical>()
			val inputFile = "../input/inputDay14Test$i.txt"
			val goal = readInputFile(inputFile, chemicalFormulas)
			assertEquals(goal, calculateTotalOreRequired(chemicalFormulas))
		}
	}

	@Test
	fun day14a() {
		val chemicalFormulas = HashMap<String, Chemical>()
		val inputFile = "../input/inputDay14.txt"
		readInputFile(inputFile, chemicalFormulas)
		val totalOreRequired = calculateTotalOreRequired(chemicalFormulas)
		println("Max integer value: ${Integer.MAX_VALUE}, answer: $totalOreRequired")
		assertEquals(ANSWER_A, totalOreRequired)
	}

	@Test
	fun testDay14b() {
		val targetFuelAmount = arrayListOf(82892753, 5586022, 460664)

		for (i in 2..4) {
			val chemicalFormulas = HashMap<String, Chemical>()
			val inputFile = "../input/inputDay14Test$i.txt"
			val goal = targetFuelAmount[i - 2]
			readInputFile(inputFile, chemicalFormulas)
			val oreRequiredPerFuel = calculateTotalOreRequired(chemicalFormulas)
			val maxAmountFuel = (TRILLION + oreRequiredPerFuel -1) / oreRequiredPerFuel
			println("Max amount of fuel for case $i: $maxAmountFuel, expected: $goal")


		}
	}

	private fun calculateTotalOreRequired(chemicalFormulas: MutableMap<String, Chemical>): Int {
		val targetChemical = chemicalFormulas[ULTIMATE_PRODUCT]
			?: throw IllegalArgumentException("Fuel chemical formula not specified!")
		targetChemical.totalRequired = 1
		determineRequiredQuantities(targetChemical)
//		println("Chemical requirements tree:")
//		println(chemicalTreeToString(targetChemical, chemicalFormulas, 0))
		chemicalFormulas.values.forEach { calculateRequiredOre(it) }
		return chemicalFormulas.values.fold(0, { acc, chemical -> acc + chemical.oreRequired })
	}

	private fun determineRequiredQuantities(chemicalToCreate: Chemical) {
		val queue = LinkedList<Chemical>()
		queue.add(chemicalToCreate)
		while (queue.isNotEmpty()) {
			val product = queue.poll()
			println("Now determining requirements for production of chemical $product")
			for (component in product.componentsRequired) {
				val chemical = component.key
				chemical.totalRequired += product.calculateRequiredBatches() * component.value
				chemical.requiredInComponents.remove(product)
				if (chemical.requiredInComponents.isEmpty()) queue.add(chemical)
			}
		}
	}

	private fun calculateRequiredOre(chemical: Chemical) {
		val oreEntry = chemical.componentsRequired.entries.find { it.key.name == ORE }
		if (oreEntry == null) chemical.oreRequired = 0
		else chemical.oreRequired = chemical.calculateRequiredBatches() * oreEntry.value
	}

	private fun readInputFile(inputFile: String, chemicalFormulas: MutableMap<String, Chemical>): Int {

		var goal = 0
		val resource = this::class.java.getResource(inputFile) ?: return goal

		resource.readText().reader().readLines().forEach {
			if (it.startsWith(GOAL_INDICATOR)) {
				// This is a test case which includes the target amount of ore required.
				goal = it.split("\\s".toRegex())[1].toInt()
			} else {
				val nanoFactory = NanoFactory(chemicalFormulas)
				nanoFactory.processFormula(it)
			}
		}
		return goal
	}

	private fun chemicalTreeToString(chemicalToPrint: Chemical, chemicalFormulas: MutableMap<String, Chemical>, indent: Int): String{
		var toString = ""
		for (i in 0..indent) {
			toString += "--"
		}
		val nextIndent = indent + 1

		toString += chemicalToPrint.totalRequired
		toString += " " + chemicalToPrint.name
		toString += System.lineSeparator()
		if (chemicalToPrint.oreRequired > 0) {
			for (i in 0..nextIndent) {
				toString += "--"
			}
			toString += "ORE: ${chemicalToPrint.oreRequired}"
			toString += System.lineSeparator()
		}

		for (component in chemicalToPrint.componentsRequired.keys) {
			toString += chemicalTreeToString(component, chemicalFormulas, nextIndent)
		}

		return toString
	}
}