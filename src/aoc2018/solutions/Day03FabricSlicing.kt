package aoc2018.solutions

import util.FileConstants
import aoc2018.dao.Claim
import java.io.File
import aoc2018.dao.IntMatrix

fun main() {
	
	val inputFile = FileConstants.INPUT_DIR + "2018\\inputDay3.txt"
	val claims = ArrayList<Claim>()
	
	File(inputFile).forEachLine {
		val numberRegex = "[^\\d]".toRegex()
		val numbers = numberRegex.split(it).filter { it.isNotBlank() }
		val integers = numbers.map{ it.toInt() }
		val newClaim = Claim(integers[0],
				Pair(integers[1], integers[2]),
				integers[3],
				integers[4])
		claims.add(newClaim)
	}
	
	val fabric = IntMatrix(0)
	// So...that Claim class is pretty useless...but at least I learned about data classes.
	for (claim in claims) {
		layClaim(claim, fabric, claims)
	}
	
	var totalDoubleClaimed = 0
	
	fabric.forEach { if (it == -1) totalDoubleClaimed++ }

	// Log answer to console. Target: 110383
	println("Number of squares that were claimed more than once: $totalDoubleClaimed")
	
	// For challenge B, find the id of the claim that doesn't overlap any other claims.
	val nonOverlappingClaim = claims.firstOrNull { !it.overlaps }
	// Log answer to challenge B to console. Target: 129
	println(nonOverlappingClaim?.claimId)
}

fun layClaim(claim: Claim, fabric: IntMatrix, claims: ArrayList<Claim>) {
	for (index in 0 until claim.height) {
		for (innerIndex in 0 until claim.width) {
			val yIndex = claim.upperLeftStart.second + index
			val xIndex = claim.upperLeftStart.first + innerIndex
			var numberToSet = claim.claimId
			// If this square has been claimed, set -1 to signify the double claim.
			val valueAtSquare = fabric.get(xIndex, yIndex) 
			if (valueAtSquare != 0) {
				// Mark the first claim as overlapping, then mark the current claim.
				if (valueAtSquare != -1) claims.get(fabric.get(xIndex, yIndex) - 1).overlaps = true
				claim.overlaps = true
				numberToSet = -1
			}
			fabric.set(Pair(xIndex, yIndex), numberToSet)
		}
	}
}
