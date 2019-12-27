package aoc2019.solutions

import aoc2019.dao.OrbitalObject
import org.junit.Test
import util.FileConstants
import java.io.File
import kotlin.test.assertEquals

class Day06OrbitMap {

	@Test
	fun day6b() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay6.txt"
		val minimumRequiredTransfers = determineMinimalRequiredTransfers(inputFile)
		println("Found minimum of required transfers to get Santa to you: $minimumRequiredTransfers")
		assertEquals(382, minimumRequiredTransfers, "Required transfers does not match expected minimum!")
	}

	@Test
	fun testDay6b() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay6bTest.txt"
		val minimumRequiredTransfers = determineMinimalRequiredTransfers(inputFile)
		println("Found minimum of required transfers to get Santa to you: $minimumRequiredTransfers")
		assertEquals(4, minimumRequiredTransfers, "Required transfers does not match expected minimum!")
	}

	private fun determineMinimalRequiredTransfers(inputFile: String): Int {
		val objectsMap = readInput(inputFile)
		// Get santa and yourself from the map.
		val santa = objectsMap["SAN"]
		val you = objectsMap["YOU"]
		// Both objects should be present in the map.
		if (santa == null || you == null) return 0
		// Calculate the number of required transfers by adding together the transfers needed to get santa and you
		// to the closest parent you have in common.
		return determineRequiredTransfers(santa, you)
	}

	private fun determineRequiredTransfers(santa: OrbitalObject, you: OrbitalObject): Int {
		var santaParent = santa.parent
		var yourParent = you.parent
		var requiredTransfersSanta = 0
		var requiredTransfersYou = 0

		outer@while (true) {
			requiredTransfersSanta = 0
			while (santaParent != null) {
				if (santaParent == yourParent) {
					break@outer
				}
				requiredTransfersSanta++
				santaParent = santaParent.parent
			}
			// Reset the Santa parent variable to the original parent.
			santaParent = santa.parent
			// Update the outer parent and the corresponding number of required transfers.
			yourParent = yourParent?.parent
			requiredTransfersYou++
		}
		println("Minimal transfers necessary to get Santa to you: ${requiredTransfersSanta + requiredTransfersYou}")
		return requiredTransfersSanta + requiredTransfersYou
	}

	@Test
	fun day6A() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay6.txt"
		val objectsMap = readInput(inputFile)
		// Determine the center of the orbital map. If none can be found, something went wrong.
		val centralObject = objectsMap.values.find { it.parent == null } ?: return
		val totalNrOfDirectAndIndirectOrbits = determineNumberOfOrbits(centralObject, 0)
		assertEquals(261306, totalNrOfDirectAndIndirectOrbits, "Incorrect number of orbits!")
	}

	@Test
	fun testDay6A() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay6Test.txt"
		val objectsMap = readInput(inputFile)

		// Determine the center of the orbital map. If none can be found, something went wrong.
		val centralObject = objectsMap.values.find { it.parent == null } ?: return
		val totalNrOfDirectAndIndirectOrbits = determineNumberOfOrbits(centralObject, 0)
		assertEquals(42, totalNrOfDirectAndIndirectOrbits, "Incorrect number of orbits!")
	}

	private fun determineNumberOfOrbits(orbitalObject: OrbitalObject, parentOrbits: Int): Int {
		var orbits = parentOrbits
		for (child in orbitalObject.children) {
			orbits += determineNumberOfOrbits(child, parentOrbits + 1)
		}
		return orbits
	}

	@Test
	fun testReadInput() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay6Test.txt"
		val objectsMap = readInput(inputFile)
		assertEquals(1, objectsMap.values.count { it.parent == null }, "More than one object without parent!")
	}

	private fun readInput(inputFile: String): Map<String, OrbitalObject> {
		val orbitalRelations = HashMap<String, OrbitalObject>()
		File(inputFile).forEachLine {
			val objects = it.split(")")
			val parent = orbitalRelations.computeIfAbsent(objects[0]) {
				OrbitalObject(
					objects[0]
				)
			}
			val child = orbitalRelations.computeIfAbsent(objects[1]) {
				OrbitalObject(
					objects[1]
				)
			}
			parent.children.add(child)
			child.parent = parent
		}
		return orbitalRelations
	}
}