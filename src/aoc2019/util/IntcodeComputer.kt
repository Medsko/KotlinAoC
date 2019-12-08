package aoc2019.util

const val OPCODE_TERMINATE = 99
// Add the integers at the positions specified by the next two numbers together, and store them in the position
// specified by the number after that (overwriting the value of that position).
const val OPCODE_ADD = 1
// Same as add, but multiply.
const val OPCODE_MULTIPLY = 2

class IntcodeComputer(private val valuesPerInstruction: Int) {

	fun runProgram(program: Array<Int>) {
		var iteration = 0
		while (!evaluateInput(program, iteration)) {
			iteration++
		}
	}

	/**
	 *
	 * @return {@code true} if the program has ended.
	 */
	fun evaluateInput(program: Array<Int>, iteration: Int): Boolean {
		val index = iteration * valuesPerInstruction
		val firstPointer = program[index + 1]
		val secondPointer = program[index + 2]
		val storagePointer = program[index + 3]

		when (program[index]) {
			OPCODE_ADD -> {
				// Get the value at the indices specified by the next int and the one after that. Add them together.
				val newValue = program[firstPointer] + program[secondPointer]
				program[storagePointer] = newValue
			}
			OPCODE_MULTIPLY -> {
				// Get the value at the indices specified by the next int and the one after that. Multiply them by each other.
				val newValue = program[firstPointer] * program[secondPointer]
				program[storagePointer] = newValue
			}
			OPCODE_TERMINATE -> {
				return true
			}
		}
		return false
	}

	fun restoreProgramToAlarmState(program: Array<Int>) {
		// "before running the program, replace position 1 with the value 12 and replace position 2 with the value 2"
		program[1] = 12
		program[2] = 2
	}

}