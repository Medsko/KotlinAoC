package aoc2019.util

enum class Opcode(val intCode: Int, val parameters: Int) {
	// Terminates the program.
	TERMINATE(99, 0),
	// Add the integers at the positions specified by the next two numbers together, and store them in the position
	// specified by the number after that (overwriting the value of that position).
	ADD(1, 3),
	// Same as add, but multiply.
	MULTIPLY(2, 3),
	// Takes a single integer as input and saves it to the position given by its only parameter.
	INPUT(3, 1),
	// Outputs the value of its only parameter.
	OUTPUT(4, 1),
	// If the first parameter is non-zero, it sets the instruction pointer to the value from the second parameter.
	// Otherwise, it does nothing.
	JUMP_IF_TRUE(5, 2),
	// If the first parameter is zero, it sets the instruction pointer to the value from the second parameter.
	// Otherwise, it does nothing.
	JUMP_IF_FALSE(6, 2),
	// If the first parameter is less than the second parameter, it stores 1 in the position given by the third
	// parameter. Otherwise, it stores 0.
	LESS_THAN(7, 3),
	// If the first parameter is equal to the second parameter, it stores 1 in the position given by the third
	// parameter. Otherwise, it stores 0.
	EQUALS(8, 3),
	// Adjusts the relative base by the value of its only parameter.
	RELATIVE_BASE_OFFSET(9, 1),

	ERROR(-1, 0);

	companion object Factory {
		fun fromCode(intCode: Int): Opcode {
			for (opcode in values()) {
				if (opcode.intCode == intCode) return opcode
			}
			return ERROR
		}
	}
}