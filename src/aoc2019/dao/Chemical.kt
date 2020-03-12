package aoc2019.dao

data class Chemical(val name: String, var batchSize: Int = 0, var totalRequired: Long = 0, var oreRequired: Long = 0) {

	val componentsRequired: MutableMap<Chemical, Int> = HashMap()

	val requiredInComponents: MutableSet<Chemical> = HashSet()

	private var totalRequiredWhenCalculateRequiredBatchesCalled = 0L

	fun calculateRequiredBatches(): Long {
		if (totalRequiredWhenCalculateRequiredBatchesCalled > 0
			&& totalRequiredWhenCalculateRequiredBatchesCalled != totalRequired)
			throw IllegalStateException("calculateRequiredBatches() has already been called before with a different value for totalRequired!")
		totalRequiredWhenCalculateRequiredBatchesCalled = totalRequired
		return (totalRequired + batchSize -1) / batchSize
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Chemical

		if (name != other.name) return false

		return true
	}

	override fun hashCode(): Int {
		return name.hashCode()
	}

}