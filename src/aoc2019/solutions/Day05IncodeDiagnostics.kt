package aoc2019.solutions

import aoc2019.util.IntcodeComputer
import org.junit.Test
import util.FileConstants
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day05IncodeDiagnostics {

	@Test
	fun day5a() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay5.txt"
		val program = readInput(inputFile)
		// Provide the computer 1 as its only input.
		val computer = IntcodeComputer(1)
		computer.startProgram(program)
		// The last output is the answer to the challenge. The other outputs should be 0.
		val testOutputs = computer.outputs
		val answer = testOutputs[testOutputs.size - 1]
		testOutputs.remove(answer)
		assertTrue("One or more tests returned an error code!") { computer.outputs.none { it > 0} }
		assertEquals(13787043, answer, "Last output did not match expected output!")
		println("Day 5 A ran successfully!")
	}

	@Test
	fun day5b() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay5.txt"
		val program = readInput(inputFile)
		// Provide the computer 5 as its only input.
		val computer = IntcodeComputer(5)
		computer.startProgram(program)
		// There should be only one output, which is the answer to the challenge.
		assertEquals(1, computer.outputs.size, "Not the correct number of outputs!")
		val answer = computer.outputs[0]
		assertEquals(3892695, answer, "Output did not match expected output!")
		println("Day 5 B ran successfully!")
	}

	@Test
	fun testDay5b() {
		val program = arrayOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
			1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
			999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99)

		// The above example program uses an input instruction to ask for a single number. The program will then
		// output 999 if the input value is below 8...
		var computer = IntcodeComputer(5)
		computer.startProgram(program)
		assertEquals(999, computer.outputs[0], "Output did not match expected output!")
		// ...output 1000 if the input value is equal to 8...
		computer = IntcodeComputer(8)
		computer.startProgram(program)
		assertEquals(1000, computer.outputs[0], "Output did not match expected output!")
		// ...output 1001 if the input value is greater than 8.
		computer = IntcodeComputer(10)
		computer.startProgram(program)
		assertEquals(1001, computer.outputs[0], "Output did not match expected output!")
	}

	private fun readInput(inputFile: String): Array<Int> {
		val rawInput = ArrayList<String>()
		File(inputFile).forEachLine {
			rawInput.addAll(it.split(","))
		}
		return rawInput.map { Integer.parseInt(it) }.toTypedArray()
	}
}