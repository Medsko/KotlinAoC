package aoc2019.solutions

import aoc2019.solutions.Day02Opcodes
import aoc2019.solutions.Day05IncodeDiagnostics
import aoc2019.solutions.Day07AmplificationCircuit
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
	Day02Opcodes::class,
	Day05IncodeDiagnostics::class,
	Day07AmplificationCircuit::class
)
class IntcodeComputerTestSuite