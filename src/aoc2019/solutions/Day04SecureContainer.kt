package aoc2019.solutions

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day04SecureContainer {
	// This poor regex worked wonderfully for challenge A - but proved useless for challenge B.
	val regexA = "(\\d)\\1".toRegex()

	private val atLeastTwo: (Int)->Boolean = { count -> count >= 2 }
	private val exactlyTwo: (Int)->Boolean = { count -> count == 2 }
	private val range = Pair(178416, 676461)

	@Test
	fun challengeA() {
		assertEquals(1650, findValidPasswords(range, atLeastTwo).size)
	}

	@Test
	fun challengeB() {
		assertEquals(1129, findValidPasswords(range, exactlyTwo).size)
	}

	@Test
	fun testChallengeA() {
		val testPasswords = listOf(112233, 123444, 111222, 115555)
		for (testPassword in testPasswords) {
			assertTrue(isValidPassword(testPassword, atLeastTwo))
		}
	}

	@Test
	fun testChallengeB() {
		assertEquals(true, isValidPassword(112233, exactlyTwo))
		assertEquals(false, isValidPassword(123444, exactlyTwo))
		assertEquals(false, isValidPassword(111222, exactlyTwo))
		assertEquals(true, isValidPassword(115555, exactlyTwo))
	}

	fun findValidPasswords(range: Pair<Int, Int>, countTest: (Int)-> Boolean): ArrayList<Int> {
		val validPasswords = ArrayList<Int>()
		// Puzzle input: range from 178416 up to and including 676461.
		for (password in range.first..range.second) {
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
}