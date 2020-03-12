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

class Day14SpaceStoichiometry {

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
		val goal = readInputFile(inputFile, chemicalFormulas)
		val totalOreRequired = calculateTotalOreRequired(chemicalFormulas)
		println("Max integer value: ${Integer.MAX_VALUE}, answer: $totalOreRequired")
		assertEquals(goal, totalOreRequired)
	}

	private fun calculateTotalOreRequired(chemicalFormulas: MutableMap<String, Chemical>): Int {
		val targetChemical = chemicalFormulas[ULTIMATE_PRODUCT]
			?: throw IllegalArgumentException("Fuel chemical formula not specified!")
		targetChemical.totalRequired = 1
		determineRequiredQuantities(targetChemical, chemicalFormulas)
		println("Chemical requirements tree:")
		println(chemicalTreeToString(targetChemical, chemicalFormulas, 0))
		chemicalFormulas.values.forEach { calculateRequiredOre(it) }
		return chemicalFormulas.values.fold(0, { acc, chemical -> acc + chemical.oreRequired })
	}


	private fun determineRequiredQuantities(chemicalToCreate: Chemical, chemicalFormulas: MutableMap<String, Chemical>) {
		// As the tree is traversed by breadth first search, this implementation only works if the tree is ordered
		// completely hierarchical (which is not the case for chemical WPTQ in test case 4, for instance).
		val queue = LinkedList<Chemical>()
		queue.add(chemicalToCreate)
		while (queue.isNotEmpty()) {
			val product = queue.poll()
			println("Now determining requirements for production of chemical $product")
			for (component in product.componentsRequired) {
				val chemical = component.key
				println("Number of ${chemical.name} required before processing ${product.name}: ${chemical.totalRequired}")
				chemical.totalRequired += product.calculateRequiredBatches() * component.value
				println("Number of ${chemical.name} required after processing ${product.name}: ${chemical.totalRequired}")
				if (!queue.contains(chemical)) queue.add(chemical)
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
//				val product = nanoFactory.product
//				if (product != null) chemicalFormulas[product.name] = product
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