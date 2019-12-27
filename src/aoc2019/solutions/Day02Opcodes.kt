package aoc2019.solutions

import aoc2019.util.IntcodeComputer
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import util.FileConstants
import java.io.File

class Day02Opcodes {

	@Test
	fun day2a() {
		assertEquals(3654868, challengeA(), "Challenge A did not yield the expected result!")
	}

	@Test
	fun day2b() {
		assertEquals(7014, challengeB(), "Challenge B did not yield the expected result!")
	}

	@Test
	fun testDay2a() {
		assertEquals(3500, testA())
	}

	private fun challengeB(): Int {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay2.txt"
		val program = readInput(inputFile)

		val targetOutcome = 19690720
		var winningNoun = 0
		var winningVerb = 0

		for (first in 0..99) {
			for (second in 0..99) {
				val computer = IntcodeComputer()
				val programVersion = program.copyOf()
				programVersion[1] = first
				programVersion[2] = second
				computer.startProgram(programVersion)

				if (programVersion[0] == targetOutcome) {
					winningNoun = first
					winningVerb = second
					break
				}
			}
		}

		println("Wining noun: $winningNoun, winning verb: $winningVerb")
		println("Answer to challenge b: ${100 * winningNoun + winningVerb}")

		return 100 * winningNoun + winningVerb
	}

	private fun challengeA(): Int {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay2.txt"
		val program = readInput(inputFile)
		val computer = IntcodeComputer()
		computer.restoreProgramToAlarmState(program)
		computer.startProgram(program)
		println("Value at position 0 after running program: ${program[0]}")
		return program[0]
	}

	private fun testA(): Int {
		val computer = IntcodeComputer()
		val program = arrayOf(1,9,10,3,2,3,11,0,99,30,40,50)
		computer.startProgram(program)
		println("Value at position 0 after running program: ${program[0]}")
		println("Program state: ${program.contentToString()}")
		return program[0]
	}

	private fun readInput(inputFile: String): Array<Int> {
		val rawInput = ArrayList<String>()
		File(inputFile).forEachLine {
			rawInput.addAll(it.split(","))
		}
		return rawInput.map { Integer.parseInt(it) }.toTypedArray()
	}
}
