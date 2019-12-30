package aoc2019.solutions

import aoc2019.util.EmergencyHullPaintingRobot
import org.junit.Test
import util.FileConstants
import util.MatrixUtils
import kotlin.test.assertEquals

class Day11SpacePolice {

	val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay11.txt"

	@Test
	fun challengeB() {
		val program = FileConstants.readProgramInputLong(inputFile)
		val robot = EmergencyHullPaintingRobot(program)
		robot.paintHull(1)
		val hull = robot.paintedPanels
		assertEquals(249, hull.size)
		println("Result image of hull for challenge 11 B:")
		printHull(hull)	// Results in CBLPJZCU being printed.
	}

	@Test
	fun challengeA() {
		val program = FileConstants.readProgramInputLong(inputFile)
		val robot = EmergencyHullPaintingRobot(program)
		robot.paintHull()
		val hull = robot.paintedPanels
		println("Result image of hull for challenge 11 A:")
		printHull(hull)
		assertEquals(2276, hull.size)
	}

	@Test
	fun testPaintAndMove() {
		val robot = EmergencyHullPaintingRobot(Array(0) { 0L })
		robot.paintAndMove(1, 0)
		println("Image after move 1: ")
		printHull(robot.paintedPanels)
		robot.paintAndMove(0, 0)
		println("Image after move 2: ")
		printHull(robot.paintedPanels)
		robot.paintAndMove(1, 0)
		robot.paintAndMove(1, 0)
		println("Image after move 4: ")
		printHull(robot.paintedPanels)
		robot.paintAndMove(0, 1)
		robot.paintAndMove(1, 0)
		robot.paintAndMove(1, 0)
		println("Final image: ")
		printHull(robot.paintedPanels)
	}

	private fun printHull(paintedPanels: Map<Pair<Int, Int>, Int>) {
		val hull = MatrixUtils.plotOnMatrix(paintedPanels, 1)
		MatrixUtils.printImage(hull)
	}

}