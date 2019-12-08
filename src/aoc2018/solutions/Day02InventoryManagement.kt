package aoc2018.solutions

import util.FileConstants
import java.io.File

fun main() {
	
	var totalTwice = 0
	var totalThrice = 0
	
	val inputFile = FileConstants.INPUT_DIR + "2018/inputDay2.txt"
	
	File(inputFile).forEachLine() {
		val letterCounts = HashMap<Char, Int>()
		// Count the occurrences of each letter in the box id.
		for (letter in it) {
			var count = letterCounts.get(letter)
			if (count == null)
				count = 0
			letterCounts.put(letter, count + 1)			
		}
		// Find the letters that occur exactly twice or thrice, and increment total accordingly.
		// Each double or triple occurrence only counts once towards the total.
		for (letter in letterCounts.keys) {
			if (letterCounts.get(letter) == 2) {
				totalTwice++
				break
			}
		}
		// For some reason, I didn't think of directly accessing the values in the map in my Kotlin
		// solution...stole this from my Java solution after solving the puzzle. 
		for (count in letterCounts.values) {
			if (count == 3) {
				totalThrice++
				break
			}
		}
	}
	// Log answer to challenge A to console. Target: 5904
	println("Checksum of box ids: " + totalTwice * totalThrice)
	
	// For challenge B, find the two boxes which id's differ exactly one letter.
	// The answer is the letters that these two box id's have in common.
	
	val lines = File(inputFile).readLines()
	var commonChars = ""
	
	outer@ for ((index, boxId) in lines.withIndex()) {
		for (innerIndex in index until lines.size) {
			// Construct a String of characters that the two boxId's have in common.
			commonChars = findCommonChars(boxId, lines.get(innerIndex))
			// If the result is exactly one letter shorter than a box id, we have our answer.
			if (commonChars.length == boxId.length - 1) break@outer
		}
	}
	
	// Log answer to challenge B to console. Target: jiwamotgsfrudclzbyzkhlrvp
	println("Letters common between two correct box id's: $commonChars")
	// Oh my...String comparison in Kotlin is pretty nice:
	if (commonChars == "jiwamotgsfrudclzbyzkhlrvp") println("Correct answer!")
}

/**
 * Composes a String of characters that the two given Strings have in common.
 */
fun findCommonChars(boxId: String, otherBoxId: String): String {
	var commonChars = ""
	for ((index, char) in boxId.withIndex()) {
		if (char == otherBoxId.get(index))
			commonChars += char
	}	
	return commonChars
}
