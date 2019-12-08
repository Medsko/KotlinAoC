package aoc2019.solutions


fun main() {
	//
	val regexA = "(\\d)\\1".toRegex()

	val atLeastTwo: (Int)->Boolean = { count -> count >= 2 }
	val exactlyTwo: (Int)->Boolean = { count -> count == 2 }

	// Tests
	val testPasswords = listOf(112233, 123444, 111222, 115555)
	for (testPassword in testPasswords) {
		println("$testPassword is a valid password: ${isValidPassword(testPassword, atLeastTwo)}")
		println("$testPassword is a valid password: ${isValidPassword(testPassword, exactlyTwo)}")
	}

	// Answers
	println("Number of unique valid passwords part A: ${findValidPasswords(atLeastTwo).size}")
	println("Number of unique valid passwords part B: ${findValidPasswords(exactlyTwo).size}")
}

fun findValidPasswords(countTest: (Int)-> Boolean): ArrayList<Int> {
	val validPasswords = ArrayList<Int>()
	// Puzzle input: range from 178416 up to and including 676461.
	for (password in 178416..676461) {
		if (isValidPassword(password, countTest)) {
			validPasswords.add(password)
		}
	}
	return validPasswords
}

fun isValidPassword(password: Int, countTest: (Int)-> Boolean): Boolean {

	var previousDigit = 0
	for (digit in password.toString()) {
		val currentDigit = Character.digit(digit, 10)
		if (currentDigit < previousDigit) {
			return false
		}
		previousDigit = currentDigit
	}
	// At this point, we know that if a digit occurs several times in the password, these occurrences are grouped.
	// Check if there is one digit that occurs either exactly two or more than two times (depending on challenge).
	for (digit in 0..9) {
		val occurrences = password.toString().count { Character.digit(it, 10) == digit }
		if (countTest(occurrences)) {
			return true
		}
	}

	return false
}

