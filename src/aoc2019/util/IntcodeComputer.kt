package aoc2019.util

import aoc2019.dao.IntcodeInstruction
import java.util.*
import kotlin.math.abs

class IntcodeComputer() {

	private var program: Array<Long> = Array(0) { 0L }
	private var index: Int = 0
	private var exitCode = 0
	private var relativeBase = 0

	private val inputs = Stack<Long>()

	val outputs = LinkedList<Long>()

	constructor(input: Int = 0): this() {
		inputs.push(input.toLong())
	}

	constructor(program: Array<Long>): this() {
		this.program = program
	}

	/**
	 * Overloaded initialization method, for backwards compatibility.
	 */
	fun startProgram(program: Array<Int>) {
		startProgram(program.map { it.toLong() }.toTypedArray())
	}

	fun startProgram(program: Array<Long>) {
		this.program = program.copyOf()

		while (exitCode > -1) {
			executeInstruction()
		}
	}

	fun continueProgram() {
		exitCode = 0
		while (exitCode > -1) {
			executeInstruction()
		}
	}

	private fun executeInstruction() {
		// Read the opcode at the current index.
		val instruction = deconstructOpcodeAndParameterModes(read(ParameterMode.IMMEDIATE))

		// Perform the operation specified by the opcode.
		when (instruction.opcode) {
			Opcode.ADD -> {
				val newValue = read(instruction.parameterModes[0]) + read(instruction.parameterModes[1])
				write(instruction.parameterModes[2], newValue)
			}
			Opcode.MULTIPLY -> {
				val newValue = read(instruction.parameterModes[0]) * read(instruction.parameterModes[1])
				write(instruction.parameterModes[2], newValue)
			}
			Opcode.INPUT -> {
				if (inputs.empty()) {
					exitCode = -1	// Pause the program if an input value is needed, but not present.
					index--	// Set the index one back, so when the program continues, we start at the input instruction.
				} else write(instruction.parameterModes[0], inputs.pop())
			}
			Opcode.OUTPUT -> {
				val output = read(instruction.parameterModes[0])
				outputs.add(output)
			}
			Opcode.JUMP_IF_TRUE -> {
				// If the first parameter is non-zero, set the instruction pointer to the value from the second parameter.
				val first = read(instruction.parameterModes[0])
				val second = read(instruction.parameterModes[1])
				if (first != 0L) index = second.toInt()
			}
			Opcode.JUMP_IF_FALSE -> {
				// If the first parameter is zero, set the instruction pointer to the value from the second parameter.
				val first = read(instruction.parameterModes[0])
				val second = read(instruction.parameterModes[1])
				if (first == 0L) index = second.toInt()
			}
			Opcode.LESS_THAN -> {
				// If the first parameter is less than the second parameter, it stores 1 in the position given by the
				// third parameter. Otherwise, it stores 0.
				val first = read(instruction.parameterModes[0])
				val second = read(instruction.parameterModes[1])
				if (first < second) write(instruction.parameterModes[2], 1)
				else write(instruction.parameterModes[2], 0)
			}
			Opcode.EQUALS -> {
				// If the first parameter is equal to the second parameter, it stores 1 in the position given by the
				// third parameter. Otherwise, it stores 0.
				val first = read(instruction.parameterModes[0])
				val second = read(instruction.parameterModes[1])
				if (first == second) write(instruction.parameterModes[2], 1)
				else write(instruction.parameterModes[2], 0)
			}
			Opcode.RELATIVE_BASE_OFFSET -> {
				relativeBase += read(instruction.parameterModes[0]).toInt()
			}
			Opcode.ERROR -> exitCode = -1000
			Opcode.TERMINATE -> exitCode = -99
		}
	}

	private fun read(parameterMode: ParameterMode): Long {
		checkRange(parameterMode)
		return when (parameterMode) {
			ParameterMode.POSITION -> program[program[index++].toInt()]
			ParameterMode.IMMEDIATE -> program[index++]
			ParameterMode.RELATIVE -> program[relativeBase + program[index++].toInt()]
		}
	}

	private fun write(parameterMode: ParameterMode, value: Long) {
		checkRange(parameterMode)
		// Write operations will never be in immediate mode.
		if (parameterMode == ParameterMode.POSITION)
			program[program[index++].toInt()] = value
		else
			program[relativeBase + program[index++].toInt()] = value
	}

	/**
	 * Checks whether given the current parameter mode, the next read/write action would fall outside of the current
	 * program range. If that is the case, the program is resized to fit that action.
	 */
	private fun checkRange(parameterMode: ParameterMode) {
		val firstPointer = index
		// If the first pointer is out of range, the second pointer will be 0 (since all new values are initialized to 0).
		var secondPointer = 0
		if (firstPointer < program.size && parameterMode != ParameterMode.IMMEDIATE) {
			secondPointer = program[firstPointer].toInt()
		}
		if (parameterMode == ParameterMode.RELATIVE) secondPointer += relativeBase
		val extreme = arrayListOf(firstPointer, secondPointer).max()?: 0
		if (extreme < program.size) return
		// Resizing is necessary. Do so now.
		val newProgram = Array(extreme + 1) { 0L }
		program.copyInto(newProgram)
		program = newProgram
	}

	private fun deconstructOpcodeAndParameterModes(instruction: Long): IntcodeInstruction {
		val instructionString = instruction.toString().padStart(5, '0')
		// Determine the opcode.
		val opcodeIntCode = Integer.parseInt(instructionString[3].toString() + instructionString[4])
		val opcode = Opcode.fromCode(opcodeIntCode)
		val nrOfParameters = opcode.parameters
		val parameterModesString = instructionString.substring(0, 3)

		val parameterModes = parameterModesString.reversed().toCharArray()
			.copyInto(CharArray(nrOfParameters), 0, 0, nrOfParameters)
			.map { Character.digit(it, 10) }
			.map { ParameterMode.fromCode(it) }

		return IntcodeInstruction(opcode, parameterModes)
	}

	fun restoreProgramToAlarmState(program: Array<Int>) {
		// "before running the program, replace position 1 with the value 12 and replace position 2 with the value 2"
		program[1] = 12
		program[2] = 2
	}

	fun provideInput(input: Int) {
		inputs.push(input.toLong())
	}

	/**
	 * Returns the latest output as an integer, for backwards compatibility.
	 */
	fun getOutput(): Int {
		return outputs.pop().toInt()
	}

	/**
	 * Returns the latest output.
	 */
	fun getOutputLong(): Long {
		return outputs.pop()
	}

	fun hasTerminated(): Boolean {
		return abs(exitCode) == Opcode.TERMINATE.intCode
	}

	fun getValueAtPostionZero(): Int {
		return program[0].toInt()
	}

	/**
	 * Resets internal state, so the computer can be used again to run another program.
	 */
	fun reset() {
		exitCode = 0
		relativeBase = 0
		index = 0
		inputs.clear()
		outputs.clear()
	}
}