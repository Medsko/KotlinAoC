package aoc2019.util

enum class ParameterMode(val code: Int) {
	// Use the value at the position that the parameter points to.
	POSITION(0),
	// Use the value of the parameter.
	IMMEDIATE(1),
	// Use the value at the position that the parameter points to, modified by the relative base.
	RELATIVE(2);

	companion object Factory {
		fun fromCode(code: Int): ParameterMode {
			for (parameterMode in values()) {
				if (code == parameterMode.code) {
					return parameterMode
				}
			}
			return POSITION
		}
	}
}