package aoc2019.dao

import aoc2019.util.Opcode
import aoc2019.util.ParameterMode

data class IntcodeInstruction(val opcode: Opcode, val parameterModes: List<ParameterMode>)