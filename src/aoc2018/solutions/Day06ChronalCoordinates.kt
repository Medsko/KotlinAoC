package aoc2018.solutions

import util.FileConstants
import java.io.File
import aoc2018.dao.IntMatrix
import util.FunctionUtils

fun main() {
	
	// Read input from file.
	val inputFile = FileConstants.INPUT_DIR + "2018\\inputDay6.txt"
	val chronalGrid = IntMatrix()
	// Keep track of each area's name using integers.
	var currentAreaId = 1
	val validAreas = ArrayList<Int>()
	val areaSizePerCoordinate = HashMap<Pair<Int, Int>, Int>()
	val areaSizePerId = HashMap<Int, Int>()

	File(inputFile).forEachLine {
		val x = it.substring(0, it.indexOf(",")).toInt()
		val y = it.substring(it.indexOf(",") + 2, it.length).toInt()
		chronalGrid.set(Pair(x, y), currentAreaId)
		areaSizePerCoordinate[Pair(x, y)] = 0
		areaSizePerId[currentAreaId] = 0
		validAreas.add(currentAreaId++)
	}

	var areaWithinManhattanDistanceSize = 0

	for (yIndex in 0 until chronalGrid.size().first) {
		for (xIndex in 0 until chronalGrid.size().second) {

			val currentLocation = Pair(xIndex, yIndex)

			if (totalManhattanDistance(currentLocation, areaSizePerCoordinate.keys) < 10000)
				areaWithinManhattanDistanceSize++

			val closestCoordinate = areaSizePerCoordinate.keys.minBy { FunctionUtils.manhattanDistance(it, currentLocation) }
				?: return	// One coordinate should always be closest.

			if (areaSizePerCoordinate.keys.filter {
					FunctionUtils.manhattanDistance(it, currentLocation) == FunctionUtils.manhattanDistance(closestCoordinate, currentLocation)
				}.size > 1) {
				// More than one coordinate is closest to this location. This coordinate should keep a value of 0.
				continue
			}
			// Update the area size of the closest coordinate.
			val closestAreaSize = areaSizePerCoordinate[closestCoordinate] ?: return
			areaSizePerCoordinate[closestCoordinate] = closestAreaSize + 1

			val closestCoordinateId = chronalGrid.get(closestCoordinate)
			val closestAreaCurrentSize = areaSizePerId[closestCoordinateId] ?: return
			areaSizePerId[closestCoordinateId] = closestAreaCurrentSize + 1

			chronalGrid.set(currentLocation, closestCoordinateId)

			if (isInfinite(currentLocation, chronalGrid.size()))
				validAreas.remove(closestCoordinateId)
		}
	}

	// Calculate the answer to challenge A.
	var biggestNonInfiniteArea = 0
	var biggestAreaSize = 0
	for (area in validAreas) {
		var areaSize = 0
		chronalGrid.forEach { if (it == area) areaSize++ }

		if (areaSize > biggestAreaSize) {
			biggestAreaSize = areaSize
			biggestNonInfiniteArea = area
		}
	}

//	println(chronalGrid)
	println("Biggest not-infinite area: $biggestNonInfiniteArea, size: $biggestAreaSize")

	biggestNonInfiniteArea = areaSizePerId.entries
		.filter{ validAreas.contains(it.key) }
		.maxBy{ it.value }
		?.key ?: 0
	biggestAreaSize = areaSizePerId[biggestNonInfiniteArea] ?: 0

	println("Biggest not-infinite area without matrix: $biggestNonInfiniteArea, size: $biggestAreaSize")

	println("Size of area within 10000 distance of all coordinates: $areaWithinManhattanDistanceSize")

	// Find the size of the largest area that isn't infinite. Target: 3660

	// Challenge B: what is the size of the region containing all locations which have a total distance to all given
	// coordinates of less than 10000? Target: 35928
}

fun totalManhattanDistance(location: Pair<Int, Int>, coordinates: Collection<Pair<Int, Int>>): Int {
	var total = 0
	coordinates.forEach { total += FunctionUtils.manhattanDistance(location, it) }
	return total
}

fun isInfinite(location: Pair<Int, Int>, gridSize: Pair<Int, Int>): Boolean {
	return location.first == 0
			|| location.second == 0
			|| location.first == gridSize.second -1
			|| location.second == gridSize.first -1
}