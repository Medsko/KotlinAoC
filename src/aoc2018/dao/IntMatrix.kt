package aoc2018.dao

class IntMatrix (private val defaultValue: Int = 0) {
	
	var matrix = ArrayList<ArrayList<Int>>()
	
	var totalFields = 0

	// All .get() calls removed with regex \n replacement:
		// \.get\(([\w\.]*)\)
		// [$1]		- $1 = capturing group ([\w.]*)	- parentheses define capturing group.

	/**
	 * Sets the given value at the specified position in the matrix. Matrix is resized as necessary. 
	 */
	fun set(position: Pair<Int, Int>, value: Int) {
		// Resize matrix so the specified position is snugly included.
		resize(position.second + 1, position.first + 1)
		matrix[position.second][position.first] = value
	}
	
	/**
	 * Returns the value at the given position, or the default value if position is out of bounds.
	 */
	fun get(position: Pair<Int, Int>): Int {
		if (isOutOfBounds(position)) {
			println("Position $position is out of bounds!")
			return defaultValue
		}
		return matrix[position.second][position.first]
	}

	/**
	 * Returns {@code true} if the given position is out of matrix bounds.
	 */
	fun isOutOfBounds(position: Pair<Int, Int>): Boolean {
		return position.second >= matrix.size || position.first >= matrix[position.second].size
	}

	fun isOutOfBounds (xPos: Int, yPos: Int): Boolean {
		return isOutOfBounds(Pair(xPos, yPos))
	}
	
	fun get(xPos: Int, yPos: Int): Int {
		return get(Pair(xPos, yPos))
	}

	fun count(target: Int): Int {
		return matrix.fold(0) { acc, row -> acc + row.count { it == target } }
	}

	fun findFirst(target: Int): Pair<Int, Int>? {
		for (i in 0 until matrix.size) {
			for (j in 0 until matrix[i].size) {
				if (get(Pair(j, i)) == target) {
					return Pair(j, i)
				}
			}
		}
		return null
	}
	
	fun forEach(action: (field: Int) -> Unit) {
		totalFields = 0
		for (i in 0 until matrix.size) {
			for (j in 0 until matrix[i].size) {
				action(matrix[i][j])
				totalFields++
			}
		}
	}

	fun size(): Pair<Int, Int> {
		if (matrix.size == 0)
			return Pair(0,0)
		return Pair(matrix[0].size, matrix.size)
	}
	
	private fun resize(height: Int, width: Int) {
		for (i in 0..height) {
			// If the index is out of bounds, add a new row to the matrix.
			if (i >= matrix.size) 
				matrix.add(ArrayList())
			
			val currentRow = matrix[i]
			// Fill the empty fields in the (new) row with default values up to desired width.
			for (j in currentRow.size..width) {
				currentRow.add(defaultValue)
			}
		}
	}
	
	override fun toString(): String {
		var toString = ""
		matrix.forEach {
			toString += it
			toString += System.lineSeparator()
		}
		return toString
	}

	fun toCharString(): String {
		var toString = ""

		matrix.forEach { row ->
			toString += "["
			row.forEach { coordinate ->
				toString += coordinate.toChar() + ","
			}
			toString = toString.dropLast(1)
			toString += "]"
			toString += System.lineSeparator()
		}

		return toString
	}
}