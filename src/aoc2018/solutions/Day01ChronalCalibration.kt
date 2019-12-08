package aoc2018.solutions

import util.FileConstants
import java.io.File

fun main() {
	val inputFile = FileConstants.INPUT_DIR + "2018\\inputDay1.txt";
	var frequency = 0
	
	File(inputFile).forEachLine {
		frequency += it.toInt()		
	}
	// Print answer to challenge A to console. Target: 435
	println("Final frequency: $frequency")
	
	val pastFrequencies = linkedSetOf<Int>()
	var recurringFrequency: Int? = null
	frequency = 0
	var iterations = 0
	
	while (recurringFrequency == null) {
		iterations++
		
		File(inputFile).forEachLine {
			
			frequency += it.toInt()
			
			if (recurringFrequency == null && !pastFrequencies.add(frequency)) {
				recurringFrequency = frequency
				// Break the for loop.
				// While loop will break because recurringFrequency is no longer null.
				return@forEachLine
			}
		}
	}
	
	// Print answer to challenge B to console. Target: 245
	println("First frequency encountered twice, after $iterations iterations: $recurringFrequency")
}
