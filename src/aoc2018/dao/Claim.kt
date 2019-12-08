package aoc2018.dao

/**
 * Represents a claim on a piece of fabric, as used in day 3 challenge.
 */
data class Claim(val claimId: Int,
				 val upperLeftStart: Pair<Int, Int>,
				 val width: Int,
				 val height: Int) {
	var overlaps = false
}