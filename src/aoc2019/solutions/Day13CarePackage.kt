package aoc2019.solutions

import aoc2018.dao.IntMatrix
import aoc2019.util.IntcodeComputer
import org.junit.Assert.fail
import org.junit.Test
import util.FileConstants
import util.MatrixUtils
import kotlin.test.assertEquals

const val BLOCK_TILE = 2

class Day13CarePackage {

	companion object Judge {
		private const val ANSWER_A = 207
		private const val ANSWER_B = 10247
	}


	@Test
	fun challengeB() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay13.txt"
		val program = FileConstants.readProgramInput(inputFile)
		// Set memory address 0 to 2 to play for free.
		program[0] = 2
		val computer = IntcodeComputer()
		computer.startProgram(program)

		val screen = IntMatrix()
		var score = updateScreenAndScore(computer, screen, 0)

		println("Current number of block tiles: ${screen.count(BLOCK_TILE)}")

		val colorCodes = HashMap<Int, String>()
		colorCodes[1] = MatrixUtils.BLACK_BACKGROUND
		colorCodes[2] = MatrixUtils.YELLOW_BACKGROUND
		colorCodes[3] = MatrixUtils.GREEN_BACKGROUND
		colorCodes[4] = MatrixUtils.RED_BACKGROUND

		MatrixUtils.printMultiColorImage(screen.matrix.map { it.toTypedArray() }.toTypedArray(), colorCodes)

		var iteration = 0

		while (screen.count(BLOCK_TILE) > 0) {

			iteration++
			val paddleValue = 3
			val ballValue = 4

			val currentPositionPaddle = screen.findFirst(paddleValue)
			val currentPositionBall = screen.findFirst(ballValue)

			if (currentPositionPaddle == null || currentPositionBall == null) {
				fail("Paddle or ball tile could not be determined!")
				break
			}

			val optimalDirection = currentPositionBall.first.compareTo(currentPositionPaddle.first)

			computer.provideInput(optimalDirection)
			computer.continueProgram()
			score = updateScreenAndScore(computer, screen, score)
			println("Iteration $iteration, current number of block tiles: ${screen.count(BLOCK_TILE)}")

			if (iteration % 10 == 0) {
				MatrixUtils.printMultiColorImage(screen.matrix.map { it.toTypedArray() }.toTypedArray(), colorCodes)
			}
		}

		assertEquals(ANSWER_B, score, "Incorrect score!")
	}

	private fun updateScreenAndScore(computer: IntcodeComputer, screen: IntMatrix, previousScore: Int): Int {
		val displayScorePosition = Pair(-1, 0)
		var score = previousScore

		while (computer.outputs.size != 0) {
			val position = Pair(computer.getOutput(), computer.getOutput())
			if (position == displayScorePosition) score = computer.getOutput()
			else screen.set(position, computer.getOutput())
		}
		return score
	}

	@Test
	fun challengeA() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay13.txt"
		val program = FileConstants.readProgramInput(inputFile)
		val computer = IntcodeComputer()
		computer.startProgram(program)

		val outputs = computer.outputs
		assertEquals(0, outputs.size % 3)

		val screen = IntMatrix()
		while (computer.outputs.size != 0) {
			val position = Pair(computer.getOutput(), computer.getOutput())
			screen.set(position, computer.getOutput())
		}

		assertEquals(ANSWER_A, screen.count(BLOCK_TILE))
	}

}