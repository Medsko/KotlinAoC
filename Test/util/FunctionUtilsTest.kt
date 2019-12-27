package util

import org.junit.jupiter.api.Test
import java.util.*

class FunctionUtilsTest {

	@Test
	fun testPermutations() {
		val collection = arrayListOf(1,2,3)
		val permutations = FunctionUtils.permutations(collection, Stack())

		for (permutation in permutations) {
			println(permutation)
		}
	}

}