package aoc2019.solutions

import aoc2019.util.IntcodeComputer
import util.FileConstants
import java.io.File

fun main() {
//	testA()
	challengeA()	// Answer: 3654868
	challengeB()	//
}

private fun challengeB() {
	val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay2.txt"
	val program = readInput(inputFile)
	val computer = IntcodeComputer(4)

	val targetOutcome = 19690720
	var winningNoun = 0
	var winningVerb = 0

	for (first in 0..99) {
		for (second in 0..99) {
			val programVersion = program.copyOf()
			programVersion[1] = first
			programVersion[2] = second
			computer.runProgram(programVersion)

			if (programVersion[0] == targetOutcome) {
				winningNoun = first
				winningVerb = second
				break
			}
		}
	}

	println("Wining noun: $winningNoun, winning verb: $winningVerb")
	println("Answer to challenge b: ${100 * winningNoun + winningVerb}")
}

private fun challengeA() {
	val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay2.txt"
	val program = readInput(inputFile)
	val computer = IntcodeComputer(4)
	computer.restoreProgramToAlarmState(program)
	computer.runProgram(program)
	println("Value at position 0 after running program: ${program[0]}")
}

private fun testA() {
	val computer = IntcodeComputer(4)
	var program = arrayOf(1,9,10,3,2,3,11,0,99,30,40,50)
	computer.runProgram(program)
	println("Value at position 0 after running program: ${program[0]}")
	println("Program state: ${program.contentToString()}")
	program = arrayOf(2,4,4,5,99,0)
	computer.runProgram(program)
	println("Value at position 0 after running program: ${program[0]}")
	println("Program state: ${program.contentToString()}")
}

private fun readInput(inputFile: String): Array<Int> {
	val rawInput = ArrayList<String>()
	File(inputFile).forEachLine {
		rawInput.addAll(it.split(","))
	}
	return rawInput.map { Integer.parseInt(it) }.toTypedArray()
}