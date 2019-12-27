package aoc2019.solutions

import aoc2019.util.IntcodeComputer
import org.junit.Test
import util.FileConstants
import util.FunctionUtils
import java.util.*
import kotlin.test.assertEquals

class Day07AmplificationCircuit {

	@Test
	fun day7a() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay7.txt"
		val program = FileConstants.readProgramInput(inputFile)
		val allPhaseSettingsSequences = FunctionUtils.permutations(arrayListOf(0,1,2,3,4), Stack())
		findHighestOutput(program, allPhaseSettingsSequences)
	}

	@Test
	fun day7b() {
		val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay7.txt"
		val program = FileConstants.readProgramInput(inputFile)
		val allPhaseSettingsSequences = FunctionUtils.permutations(arrayListOf(5,6,7,8,9), Stack())
		assertEquals(5406484, findHighestOutput(program, allPhaseSettingsSequences))
	}

	@Test
	fun testDay7a() {
		var program = arrayOf(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0)
		val allPhaseSettingsSequences = FunctionUtils.permutations(arrayListOf(0,1,2,3,4), Stack())
		assertEquals(43210, findHighestOutput(program, allPhaseSettingsSequences), "First test case for challenge A failed!")

		program = arrayOf(3,23,3,24,1002,24,10,24,1002,23,-1,23, 101,5,23,23,1,24,23,23,4,23,99,0,0)
		assertEquals(54321, findHighestOutput(program, allPhaseSettingsSequences), "Second test case for challenge A failed!")
	}

	@Test
	fun testDay7b() {
		var program = arrayOf(3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5)
		val allPhaseSettingsSequences = FunctionUtils.permutations(arrayListOf(5,6,7,8,9), Stack())
		assertEquals(139629729, findHighestOutput(program, allPhaseSettingsSequences))

		program = arrayOf(3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,
					-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,
					53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10)
		assertEquals(18216, findHighestOutput(program, allPhaseSettingsSequences))
	}

	private fun findHighestOutput(program: Array<Int>, allPhaseSettingsSequences: Collection<Collection<Int>>): Int {
		val outputs = ArrayList<Int>()
		val outputPerSequence = HashMap<Collection<Int>, Int>()

		for (phaseSettingSequence in allPhaseSettingsSequences) {
			val output = runPhaseSettingsCombinationFeedback(program, phaseSettingSequence)
			outputs.add(output)
			outputPerSequence[phaseSettingSequence] = output
		}
		val maxOutputPair = outputPerSequence.entries.maxBy { it.value }
		val maxOutput = outputs.max()
		println("Maximum output: $maxOutput, from phase setting ${ maxOutputPair?.key }")
		return maxOutput!!
	}

	/**
	 * Runs the given program with the given sequence of phase settings.
	 * @return The final output signal that is sent to the thrusters.
	 */
	private fun runPhaseSettingsCombinationFeedback(program: Array<Int>, phaseSettings: Collection<Int>): Int {

		val amplifiers = startAmplifiers(program, phaseSettings)
		var previousOutput = amplifiers[4].getOutput()

		var loops = 0

		while (!amplifiers[4].hasTerminated()) {
			for (computer in amplifiers) {
				computer.provideInput(previousOutput)
				computer.continueProgram()
				previousOutput = computer.getOutput()
			}
			loops++
			println("End of loop $loops")
		}

		return previousOutput
	}

	private fun startAmplifiers(program: Array<Int>, phaseSettings: Collection<Int>): Array<IntcodeComputer> {
		var previousOutput = 0
		val amplifiers = Array(5) { IntcodeComputer() }
		for (phaseSetting in phaseSettings.withIndex()) {
			val computer = amplifiers[phaseSetting.index]
			// The first input is the phase setting, the second is the output of the previous computation (0 for the
			// first amplifier).
			computer.provideInput(previousOutput)
			computer.provideInput(phaseSetting.value)
			computer.startProgram(program.copyOf())
			if (phaseSetting.index < amplifiers.size - 1) previousOutput = computer.getOutput()
		}
		return amplifiers
	}

}