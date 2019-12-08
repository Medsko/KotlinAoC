package aoc2018.solutions

import util.FileConstants
import java.io.File

fun main() {
	
	val inputFile = FileConstants.INPUT_DIR + "\\2018\\inputDay5.txt"
	var polymer = File(inputFile).readText()
	
	polymer = reduce(polymer)
	
	// Print the answer to challenge A. Target: 11814
	println("Number of units remaining: ${polymer.length}")
	
	// For challenge B: find the shortest polymer after removing one character.
	var shortestPolymer = polymer.length
	var mostRemovableUnit = 'a'
	for (char in 'a'..'z') {
		val regex = Regex("[" + char + char.toUpperCase() + "]")
		val cauterizedPolymer = polymer.replace(regex, "")
		val reducedLength = reduce(cauterizedPolymer).length
		if (reducedLength < shortestPolymer) {
			shortestPolymer = reducedLength
			mostRemovableUnit = char
		}
	}
	// Print answer to challenge B. Target: 4282 (by removing 'g').
	println("Shortest polymer after removing unit $mostRemovableUnit: $shortestPolymer")
}

//@Test
fun testA() {
	// TODO
}

fun reduce(polymer: String): String {
	// Define an extension function that tests whether other Character is same but in opposite case
	// (simultaneously proving how sexy Kotlin is).
	fun Char.isPolar(other: Char): Boolean =
		if (this.isLowerCase())
			this.toUpperCase() == other	// This does the same (though opposite)...
		else
			this.toLowerCase().equals(other)	// ...as this - since '==' operator is overloaded for String.

	var index = 0
	var result = polymer
	while (index < result.length - 1) {
		if (result[index].isPolar(result[index + 1])) {
			// Now use that extension function to remove polars...owwww yeeeah...
			result = result.removeRange(index, index + 2)
			if (index > 0) index--
		} else {
			index++
		}
	}
	return result
}
