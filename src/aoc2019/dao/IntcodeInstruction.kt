package aoc2019.dao

import aoc2019.util.Opcode

data class IntcodeInstruction(val opcode: Opcode, val parameterModes: List<Int>)