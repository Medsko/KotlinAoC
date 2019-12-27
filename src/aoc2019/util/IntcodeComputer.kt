package aoc2019.util

import java.util.*
import kotlin.math.abs

const val PARAMETER_MODE_IMMEDIATE = 1

class IntcodeComputer() {

	private var program: Array<Int> = Array(0) { 0 }
	private var index: Int = 0
	private var exitCode = 0

	private val inputs = Stack<Int>()

	val outputs = LinkedList<Int>()

	constructor(input: Int = 0): this() {
		inputs.push(input)
	}

	fun startProgram(program: Array<Int>) {
		exitCode = 0
		this.program = program
		while (exitCode > -1) {
			executeInstruction()
		}
		println("Pausing/halting program with exit code: $exitCode")
	}

	fun continueProgram() {
		exitCode = 0
		while (exitCode > -1) {
			executeInstruction()
		}
		println("Pausing/halting program with exit code: $exitCode")
	}

	private fun executeInstruction() {
		// Read the opcode at the current index.
		val instruction = deconstructOpcodeAndParameterModes(read(1))

		// Perform the operation specified by the opcode.
		when (instruction.opcode) {
			Opcode.ADD -> {
				val newValue = read(instruction.parameterModes[0]) + read(instruction.parameterModes[1])
				write(newValue)
			}
			Opcode.MULTIPLY -> {
				val newValue = read(instruction.parameterModes[0]) * read(instruction.parameterModes[1])
				write(newValue)
			}
			Opcode.INPUT -> {
				if (inputs.empty()) {
					exitCode = -1	// Pause the program if an input value is needed, but not present.
					index--	// Set the index one back, so when the program continues, we start at the input instruction.
				} else write(inputs.pop())
			}
			Opcode.OUTPUT -> {
				val output = read(instruction.parameterModes[0])
				outputs.add(output)
			}
			Opcode.JUMP_IF_TRUE -> {
				// If the first parameter is non-zero, set the instruction pointer to the value from the second parameter.
				val first = read(instruction.parameterModes[0])
				val second = read(instruction.parameterModes[1])
				if (first != 0) index = second
			}
			Opcode.JUMP_IF_FALSE -> {
				// If the first parameter is zero, set the instruction pointer to the value from the second parameter.
				val first = read(instruction.parameterModes[0])
				val second = read(instruction.parameterModes[1])
				if (first == 0) index = second
			}
			Opcode.LESS_THAN -> {
				// If the first parameter is less than the second parameter, it stores 1 in the position given by the
				// third parameter. Otherwise, it stores 0.
				val first = read(instruction.parameterModes[0])
				val second = read(instruction.parameterModes[1])
				if (first < second) write(1)
				else write(0)
			}
			Opcode.EQUALS -> {
				// If the first parameter is equal to the second parameter, it stores 1 in the position given by the
				// third parameter. Otherwise, it stores 0.
				val first = read(instruction.parameterModes[0])
				val second = read(instruction.parameterModes[1])
				if (first == second) write(1)
				else write(0)
			}
			Opcode.ERROR -> exitCode = -1000
			Opcode.TERMINATE -> exitCode = -99
		}
	}

	private fun read(parameterMode: Int): Int {
		return if (parameterMode == PARAMETER_MODE_IMMEDIATE) program[index++]
			else program[program[index++]]
	}

	private fun write(value: Int) {
		// Write operations will never be in immediate mode.
		program[program[index++]] = value
	}

	private fun deconstructOpcodeAndParameterModes(instruction: Int): IntcodeInstruction {
		val instructionString = instruction.toString().padStart(5, '0')
		// Determine the opcode.
		val opcodeIntCode = Integer.parseInt(instructionString[3].toString() + instructionString[4])
		// Kotlin does not have 'static', so to use a from...() to determine a matching enum instance, a random instance
		// of that enum has to be used to be able to call that function.
		val opcode = Opcode.ERROR.fromIntCode(opcodeIntCode)
		val nrOfParameters = opcode.parameters
		val parameterModesString = instructionString.substring(0, 3)

		val parameterModes = parameterModesString.reversed().toCharArray()
			.copyInto(CharArray(nrOfParameters), 0, 0, nrOfParameters)
			.map { Character.digit(it, 10) }

		return IntcodeInstruction(opcode, parameterModes)
	}

	fun restoreProgramToAlarmState(program: Array<Int>) {
		// "before running the program, replace position 1 with the value 12 and replace position 2 with the value 2"
		program[1] = 12
		program[2] = 2
	}

	fun provideInput(input: Int) {
		inputs.push(input)
	}

	fun getOutput(): Int {
		return outputs.pop()
	}

	fun hasTerminated(): Boolean {
		return abs(exitCode) == Opcode.TERMINATE.intCode
	}
}