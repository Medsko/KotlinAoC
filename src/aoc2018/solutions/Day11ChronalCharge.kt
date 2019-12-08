package aoc2018.solutions

import aoc2018.dao.IntMatrix
import kotlin.math.max

const val GRID_SERIAL_NUMBER = 7803

fun main() {
	println("Power level of cell at 3,5 in grid with serial number 8: ${determinePowerLevel(Pair(3,5), 8)}")
	challengeA()
	challengeB()
}

private fun initializeGrid(): IntMatrix {
	val gridSize = 300
	val grid = IntMatrix()
	val gridSerialNumber = GRID_SERIAL_NUMBER

	for (yIndex in 1..gridSize) {
		for (xIndex in 1..gridSize) {
			val position = Pair(xIndex, yIndex)
			grid.set(position, determinePowerLevel(position, gridSerialNumber))
		}
	}
	return grid
}

// While this produces the correct answer, the performance of this algorithm is abominable. Optimization ideas:
// Introduce temporary 'overlap' variable, so next square requires only one column of additions + overlap

private fun challengeB() {
	val grid = initializeGrid()
	var maxPower = 0
	var maxPowerSquareUpperLeftWithSize = Triple(0, 0, 0)
	// In challenge B, grid sizes from 1x1 to 300x300 are supported.
	for (squareSize in 1..300) {
		val maxPowerSquareUpperLeft = determineMaxPowerSquare(grid, squareSize)
		val square = determineSquare(maxPowerSquareUpperLeft, grid, squareSize)
		val squarePower = square.sumBy { it.sum() }
		if (squarePower > maxPower) {
			maxPower = squarePower
			maxPowerSquareUpperLeftWithSize = Triple(maxPowerSquareUpperLeft.first, maxPowerSquareUpperLeft.second, squareSize)
		}
	}
	// Expected: 230, 272, 17
	println("Square with highest power level, upper left corner position and size: $maxPowerSquareUpperLeftWithSize")
}

private fun challengeA() {
	val grid = initializeGrid()
	val maxPowerSquareUpperLeft = determineMaxPowerSquare(grid, 3)
	val maxPowerSquare = determineSquare(maxPowerSquareUpperLeft, grid, 3)

	// Expected: position (20,51)
	println("Square with the highest power level has upper left corner position: $maxPowerSquareUpperLeft")
	for (row in maxPowerSquare) {
		println(row.contentToString())
	}
}

private fun determineMaxPowerSquare(grid: IntMatrix, squareSize: Int): Pair<Int, Int> {
	var maxPower = 0
	var maxPowerSquareUpperLeft = Pair(0, 0)
	val bound = grid.matrix.size - (squareSize - 1)

	for (yIndex in 1..bound) {
		for (xIndex in 1..bound) {
			val position = Pair(xIndex, yIndex)
			val currentSquare = determineSquare(position, grid, squareSize)
			val currentSquarePower = currentSquare.sumBy { it.sum() }
			if (currentSquarePower > maxPower) {
				maxPower = currentSquarePower
				maxPowerSquareUpperLeft = position
			}
		}
	}
	return maxPowerSquareUpperLeft
}

private fun determineSquare(position: Pair<Int, Int>, grid: IntMatrix, squareSize: Int): Array<Array<Int>> {

	val square = Array(squareSize) { Array(squareSize) { 0 } }

	for (yModifier in 0 until squareSize) {
		for (xModifier in 0 until squareSize) {
			val xPosition = position.first + xModifier
			val yPosition = position.second + yModifier
			// Check whether this
			if (grid.isOutOfBounds(xPosition, yPosition)) continue

			val powerCurrentCell = grid.get(xPosition, yPosition)
			square[yModifier][xModifier] = powerCurrentCell
		}
	}
	return square
}

/**
 * Determines the power level of the cell at the given position.
 */
private fun determinePowerLevel(position: Pair<Int, Int>, gridSerialNumber: Int): Int {
	// Determine the rack id (x coordinate + 10).
	val rackId = position.first + 10
	// Rack id times y coordinate.
	var powerLevel = rackId * position.second
	// Increase by serial number.
	powerLevel += gridSerialNumber
	// Multiply by rack id.
	powerLevel *= rackId
	// Keep only the hundreds digit of the power level.
	val powerLevelCharArray = powerLevel.toString().toCharArray()
	val hundredsDigit = Integer.parseInt(powerLevelCharArray[powerLevelCharArray.size - 3].toString())
	// Subtract five from the result.
	return hundredsDigit - 5
}