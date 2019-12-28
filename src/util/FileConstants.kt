package util

import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path


// In Kotlin, an 'object' declaration amounts to a Singleton.
object FileConstants {
	
	const val INPUT_DIR = "C:\\Users\\Medsko\\git\\AoC\\adventOfCode\\input\\"
	const val INPUT_DIR_2018 = "C:\\Users\\Medsko\\git\\AoC\\adventOfCode\\input\\2018\\"
	const val INPUT_DIR_2019 = "C:\\Users\\Medsko\\git\\AoC\\adventOfCode\\input\\2019\\"
	const val OUTPUT_DIR = "C:\\Users\\Medsko\\git\\AoC\\adventOfCode\\output\\"

	/**
	 * To fully understand this method, read javadoc for the methods
	 * [Files.notExists] and [Files.exists].
	 */
	fun fileNotExistsAndIsWritable(filePath: Path?): Boolean {
		if (filePath == null) return false
		// Determine whether the given path is valid and if the file is writable.
		return Files.notExists(filePath) || Files.exists(filePath)
	}

	/**
	 * Reads an intcode program from the given file, and returns it as an array of integer instructions.
	 */
	fun readProgramInput(inputFile: String): Array<Int> {
		return readProgramInputLong(inputFile).map { it.toInt() }.toTypedArray()
	}

	/**
	 * Reads an intcode program from the given file, and returns it as an array of long instructions.
	 */
	fun readProgramInputLong(inputFile: String): Array<Long> {
		val rawInput = ArrayList<String>()
		File(inputFile).forEachLine {
			rawInput.addAll(it.split(","))
		}
		return rawInput.map { it.toLong() }.toTypedArray()
	}
}

fun main() {
	// Open the input folder.
	val desktop = Desktop.getDesktop()
	val inputDir = File(FileConstants.INPUT_DIR_2019)
	try {
		desktop.open(inputDir)
	} catch (e: IOException) {
		e.printStackTrace()
	}

}