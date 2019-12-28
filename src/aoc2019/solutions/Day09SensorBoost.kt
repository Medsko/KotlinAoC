package aoc2019.solutions

import aoc2019.util.IntcodeComputer
import aoc2019.util.Opcode
import org.junit.Test
import util.FileConstants
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day09SensorBoost {

	val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay9.txt"

	@Test
	fun challengeB() {
		val program = FileConstants.readProgramInputLong(inputFile)
		val computer = IntcodeComputer()
		computer.provideInput(2)	// Run in for realsies mode by providing a 2 as sole input value.
		computer.startProgram(program)
		// If all goes right, the program should output only one value.
		assertEquals(1, computer.outputs.size)
		assertEquals(59785, computer.getOutputLong())
	}

	@Test
	fun challengeA() {
		val program = FileConstants.readProgramInputLong(inputFile)
		val computer = IntcodeComputer()
		computer.provideInput(1)	// Run in test mode by providing a 1 as sole input value.
		computer.startProgram(program)
		// If all goes right, the program should output only one value.
		assertEquals(1, computer.outputs.size)
		assertEquals(3906448201, computer.getOutputLong())
	}

	@Test
	fun testChallengeA() {
		var program = arrayOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99).map { it.toLong() }.toTypedArray()
		val computer = IntcodeComputer()
		computer.startProgram(program)
		// Takes no input and should produce a copy of itself as output.
		val output = computer.outputs.toTypedArray()
		assertTrue(Arrays.deepEquals(output, program))

		computer.reset()	// Reuse the same computer, to test whether state is reset appropriately.
		program = arrayOf(1102,34915192L,34915192,7,4,7,99,0)
		computer.provideInput(1)	// It's not specified whether the other programs take input, provide test input just in case.
		computer.startProgram(program)	// Should output a 16-digit number.
		assertEquals(16, computer.getOutputLong().toString().toCharArray().size)

		computer.reset()
		program = arrayOf(104,1125899906842624,99)
		computer.startProgram(program)	// Should output the big number in the middle of the program.
		assertEquals(1125899906842624, computer.getOutputLong())
	}

	@Test
	fun testResizing() {
		// When run, this should multiply 20 (value at index 2) by 0 (since 20 is out of range and should be initialized
		// to zero) and store it at index zero - then terminate.
		val program = arrayOf(Opcode.MULTIPLY.intCode, 2, 20, 0, 99).map { it.toLong() }.toTypedArray()
		val computer = IntcodeComputer()
		computer.startProgram(program)
		assertEquals(0, computer.getValueAtPostionZero())
	}
}