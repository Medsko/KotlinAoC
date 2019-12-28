package aoc2019.solutions

import org.junit.Test
import util.FileConstants
import java.io.File
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day08SpaceImageFormatDecoder {

	private val inputFile = FileConstants.INPUT_DIR_2019 + "inputDay8.txt"

	val WHITE_FOREGROUND = "\u001B[37m"
	val BLACK_BACKGROUND = "\u001B[40m"
	val WHITE_BACKGROUND = "\u001B[47m"

	val COLOR_RESET = "\u001B[0m"

	@Test
	fun challengeB() {
		val pixels = readInput(inputFile)
		val imageLayers = pixelsToImageLayers(pixels, 25, 6)
		val finalImage = determineFinalImage(imageLayers, createImageLayer(25, 6))
		finalImage.forEach { it.reverse() }
		printImage(finalImage.reversedArray())
		// Prints 'CEKUA'
		val topRow = arrayOf(0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0)
		assertTrue(topRow.contentDeepEquals(finalImage.reversedArray()[0]))
	}

	@Test
	fun testChallengeB() {
		val imageData = "0222112222120000"
		val pixels = splitInput(imageData)
		val imageLayers = pixelsToImageLayers(pixels, 2, 2)
		val finalImage = determineFinalImage(imageLayers, createImageLayer(2, 2))
		printImage(finalImage)
	}

	@Test
	fun challengeA() {
		val pixels = readInput(inputFile)
		val imageLayers = pixelsToImageLayers(pixels, 25, 6)
		val imageLayerWithFewestZeros = findImageLayerWithFewestZeros(imageLayers)?: return
		val oneCount = countOccurrences(1, imageLayerWithFewestZeros)
		val twoCount = countOccurrences(2, imageLayerWithFewestZeros)
		assertEquals(1548, oneCount * twoCount)
	}

	@Test
	fun testChallengeA() {
		val imageData = "122456789012"
		val pixels = splitInput(imageData)
		val imageLayers = pixelsToImageLayers(pixels, 3, 2)
		val imageLayerWithFewestZeros = findImageLayerWithFewestZeros(imageLayers)?: return
		for (row in imageLayerWithFewestZeros) {
			println(row.contentToString())
		}
		val oneCount = countOccurrences(1, imageLayerWithFewestZeros)
		val twoCount = countOccurrences(2, imageLayerWithFewestZeros)
		assertEquals(1, oneCount)
		assertEquals(2, twoCount)
		assertEquals(2, oneCount * twoCount)
	}

	/**
	 * Prints the given image to the console. If the value of a cell is 0, that pixel is printed black.
	 * If the value is 1, that pixel is printed in 'white'.
	 */
	private fun printImage(image: Array<Array<Int>>) {
		println("Final image: ")
		for (row in image) {
			var currentRow = ""
			for (cell in row) {
				if (cell == 0) currentRow += BLACK_BACKGROUND
				else currentRow += WHITE_BACKGROUND + WHITE_FOREGROUND
				currentRow += cell
				currentRow += COLOR_RESET
			}
			println(currentRow)
		}
	}

	private fun determineFinalImage(imageLayers: List<Array<Array<Int>>>, finalImage: Array<Array<Int>>): Array<Array<Int>> {
		imageLayers.reversed().forEach { imageLayer ->
			imageLayer.indices.forEach { rowIndex ->
				imageLayer[rowIndex].indices.forEach { cellIndex ->
					if (finalImage[rowIndex][cellIndex] == 2) {
						finalImage[rowIndex][cellIndex] = imageLayer[rowIndex][cellIndex]
					}
				}
			}
		}
		return finalImage
	}

	private fun countOccurrences(value: Int, imageLayer: Array<Array<Int>>): Int {
		return imageLayer.fold(0) { acc, row -> acc + row.count { el -> el == value} }
	}

	private fun findImageLayerWithFewestZeros(imageLayers: List<Array<Array<Int>>>): Array<Array<Int>>? {
		return imageLayers.minBy { imageLayer ->
			imageLayer.fold(0) {
					acc, ints -> acc + ints.count { el -> el == 0 }
			}
		}
	}

	private fun pixelsToImageLayers(pixels: Stack<Int>, width: Int, length: Int): List<Array<Array<Int>>> {
		val imageLayers = ArrayList<Array<Array<Int>>>()
		while (pixels.isNotEmpty()) {
			val imageLayer = createImageLayer(width, length)
			fillImageLayer(imageLayer, pixels)
			imageLayers.add(imageLayer)
		}
		return imageLayers
	}


	private fun fillImageLayer(imageLayer: Array<Array<Int>>, pixels: Stack<Int>) {
		imageLayer.indices.forEach { rowIndex ->
			imageLayer[rowIndex].indices.forEach { cellIndex ->
				imageLayer[rowIndex][cellIndex] = pixels.pop()
			}
		}
	}

	private fun createImageLayer(width: Int, length: Int): Array<Array<Int>> {
		return Array(length) { Array(width) { 2 } }
	}

	private fun readInput(inputFile: String): Stack<Int> {
		val rawInput = File(inputFile).readLines().joinToString("")
		return splitInput(rawInput)
	}

	private fun splitInput(rawInput: String): Stack<Int> {
		val split = Stack<Int>()
		for (char in rawInput.toCharArray()) {
			split.push(Character.digit(char, 10))
		}
		return split
	}
}