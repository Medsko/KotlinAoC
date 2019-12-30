package util

import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class FunctionUtilsTest {

	@Test
	fun testPermutations() {
		val collection = arrayListOf(1,2,3)
		val permutations = FunctionUtils.permutations(collection, Stack())

		for (permutation in permutations) {
			println(permutation)
		}
	}

	@Test
	fun testAngleBetweenPoints() {
		val start = Pair(1,1)
		val left = Pair(0,1)
		val up = Pair(1,2)
		val leftAngle = FunctionUtils.angleBetweenPoints(start, left, false)
		val upAngle = FunctionUtils.angleBetweenPoints(start, up, false)
		assertEquals(270.0, leftAngle)
		assertEquals(0.0, upAngle)
	}

}