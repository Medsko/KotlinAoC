package aoc2019.util

class EmergencyHullPaintingRobot(program: Array<Long>) {

	private val brain = IntcodeComputer(program)
	private var currentDirection = Direction.UP
	private var currentPosition = Pair(0, 0)

	val paintedPanels = HashMap<Pair<Int, Int>, Int>()

	fun paintHull(startingPanelColor: Int = 0) {
		while (!brain.hasTerminated()) {
			val inputValue = paintedPanels.computeIfAbsent(currentPosition) { startingPanelColor }
			brain.provideInput(inputValue)
			brain.continueProgram()
			val paintColor = brain.getOutput()
			val turningDirection = brain.getOutput()
			paintAndMove(paintColor, turningDirection)
		}
	}

	fun paintAndMove(paintColor: Int, turningDirection: Int) {
		paintedPanels[currentPosition] = paintColor
		currentDirection = if (turningDirection == 0) currentDirection.turnCounterClockwise() else currentDirection.turnClockwise()
		currentPosition = currentDirection.move(currentPosition)
	}
}