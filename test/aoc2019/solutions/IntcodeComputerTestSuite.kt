package aoc2019.solutions

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
	Day02Opcodes::class,
	Day05IncodeDiagnostics::class,
	Day07AmplificationCircuit::class,
	Day09SensorBoost::class,
	Day11SpacePolice::class
)
class IntcodeComputerTestSuite