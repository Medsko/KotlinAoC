package aoc2019.solutions

import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
	Day01FuelRequirements::class,
	IntcodeComputerTestSuite::class,
	Day04SecureContainer::class,
	Day06OrbitMap::class
)
class TestSuite