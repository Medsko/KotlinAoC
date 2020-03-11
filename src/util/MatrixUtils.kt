package util

import kotlin.math.abs


object MatrixUtils {

	const val WHITE_FOREGROUND = "\u001B[37m"
	const val BLACK_BACKGROUND = "\u001B[40m"
	const val WHITE_BACKGROUND = "\u001B[47m"
	const val COLOR_RESET = "\u001B[0m"
	const val RED_BACKGROUND = "\u001B[41m"
	const val GREEN_BACKGROUND = "\u001B[42m"
	const val YELLOW_BACKGROUND = "\u001B[43m"

	/**
	 * Prints the given image to the console. If the value of a cell is 0, that pixel is printed black.
	 * If the value is 1, that pixel is printed in 'white'.
	 */
	fun printImage(image: Array<Array<Int>>) {
		for (row in image) {
			var currentRow = ""
			for (cell in row) {
				currentRow += if (cell == 0) BLACK_BACKGROUND
				else WHITE_BACKGROUND + WHITE_FOREGROUND
				currentRow += cell
				currentRow += COLOR_RESET
			}
			println(currentRow)
		}
	}

	fun printMultiColorImage(image: Array<Array<Int>>, colors: Map<Int, String>) {
		for (row in image) {
			var currentRow = ""
			for (cell in row) {
				val color = colors[cell] ?: WHITE_BACKGROUND
				currentRow += color
				currentRow += cell
				currentRow += COLOR_RESET
			}
			println(currentRow)
		}
	}

	fun plotOnMatrix(valuePerPosition: Map<Pair<Int, Int>, Int>, pad: Int): Array<Array<Int>> {
		val dummy = Array(0) { Array(0) { 0 } }
		val minimalPositionX = valuePerPosition.keys.map { it.first }.min()
		val minimalPositionY = valuePerPosition.keys.map { it.second }.min()
		if (minimalPositionX == null || minimalPositionY == null) return dummy
		val maxPositionX = valuePerPosition.keys.map { it.first }.max()
		val maxPositionY = valuePerPosition.keys.map { it.second }.max()
		if (maxPositionX == null || maxPositionY == null) return dummy
		val xModifier = if (minimalPositionX < 0) abs(minimalPositionX) + pad else pad
		val yModifier = if (minimalPositionY < 0) abs(minimalPositionY) + pad else pad
		val matrix = Array(maxPositionY + yModifier + pad + 1) { Array(maxPositionX + xModifier + pad + 1) { 0 } }
		for (position in valuePerPosition) {
			val xPosition = position.key.first + xModifier
			val yPosition = position.key.second + yModifier
			matrix[yPosition][xPosition] = position.value
		}
		return matrix
	}

}