package aoc2018.solutions

import java.lang.IllegalStateException
import java.util.*

// The input for this challenge - to read this from file would be overkill.
const val PLAYERS = 446
const val LAST_MARBLE_WORTH = 71522

fun main() {

	challengeA(9, 25)	// Expected: 32
	challengeA(10, 1618)	// Expected: 8317
	challengeA(13, 7999)	// Expected: 146373

	challengeA(PLAYERS, LAST_MARBLE_WORTH)	// Expected: 390592

	// Challenge B is challenge A with the worth of the last marble * 100.
	challengeA(PLAYERS, LAST_MARBLE_WORTH * 100)	// Expected: 3277920293
}

private fun challengeA(players: Int, lastMarbleWorth: Int) {

	val scores = Array(players) { 0L }
	val marbleCircle = LinkedList<Int>()
	// Add the first marble.
	marbleCircle.offerFirst(0)

	for (marble in 1..lastMarbleWorth) {
		if (marble % 23 == 0) {
			val currentPlayer = marble % scores.size
			marbleCircle.rotate(-7)
			var score = marble.toLong()
			score += marbleCircle.pollFirst() ?: throw IllegalStateException("Marble circle is empty!")
			scores[currentPlayer] += score
			// Set the cursor on the next marble.
			marbleCircle.rotate(1)
		} else {
			marbleCircle.rotate(1)
			marbleCircle.offerFirst(marble)
		}
	}
	val highscore = scores.max() ?: 0
	println("Highscore with $players players and last marble worth $lastMarbleWorth: $highscore")
}

fun LinkedList<Int>.rotate(clockwiseIndices: Int) {
	var rotations = clockwiseIndices
	while (rotations > 0) {
		// Rotate clockwise for the given number of indices.
		this.offerFirst(this.pollLast())
		rotations--
	}

	while (rotations < 0) {
		// Rotate counter-clockwise for the given number of indices.
		this.offerLast(this.pollFirst())
		rotations++
	}
}
