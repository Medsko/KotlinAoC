package aoc2018.solutions

import util.FileConstants
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

fun main() {

	// Read the input into a workable data structure
	val inputFile = FileConstants.INPUT_DIR + "2018\\inputDay7.txt"
	val dependencies = ArrayList<Pair<String, String>>()
	var stepNames: MutableSet<String> = HashSet()

	File(inputFile).forEachLine {
		val relations = "\\s[A-Z]\\s".toRegex().findAll(it)
		val master = relations.elementAt(0).value.trim()
		val slave = relations.elementAt(1).value.trim()
		dependencies.add(Pair(master, slave))
		stepNames.add(master)
		stepNames.add(slave)
	}
	// Order the step names alphabetically, since this is the execution order in case of a tie.
	stepNames = stepNames.toSortedSet()

	challengeA(HashSet(stepNames), dependencies)
	challengeB(stepNames, dependencies)
}

fun challengeB(sortedStepNames: MutableSet<String>, dependencies: ArrayList<Pair<String, String>>) {
	val executedSteps = HashSet<String>()
	var workers = Array(5) { Pair("", 0) }
	var secondsExecutionTime = 0

	while (true) {

		workers = workers.map { worker ->
			// Subtract 1 second from the total working time of each busy worker.
			if (worker.second > 0) Pair(worker.first, worker.second - 1) else worker
		}.map {
			// This worker is free. Mark the step it was working on as executed.
			if (it.second == 0 && it.first.isNotEmpty()) executedSteps.add(it.first)
			it
		}.map { worker ->
			if (worker.second == 0) {
				// This worker is free. Assign it the next step to execute.
				val nextStep = determineNextExecutableStep(sortedStepNames, dependencies, executedSteps)

				if (nextStep != null) {
					sortedStepNames.remove(nextStep)
					// The integer value of 'A' is 65, so correct by 4 to get to prescribed value from challenge.
					Pair(nextStep, nextStep.toCharArray()[0].toInt() - 4)
				} else worker
			} else worker
		}.toTypedArray()

		// If no workers are busy anymore, we are done.
		if (workers.none { worker -> worker.second > 0}) break

		secondsExecutionTime++

//		println("Second $secondsExecutionTime, activity: ")
//		workers.forEach {
//			println("Working on ${it.first}, seconds left: ${it.second}")
//		}
	}
	println("Seconds it took to complete all steps: $secondsExecutionTime")
}


fun challengeA(sortedStepNames: MutableSet<String>, dependencies: ArrayList<Pair<String, String>>) {
	val executedSteps = LinkedHashSet<String>()
	while (sortedStepNames.isNotEmpty()) {

		val executableStep = determineNextExecutableStep(sortedStepNames, dependencies, executedSteps)

		if (executableStep != null) {
			executedSteps.add(executableStep)
			sortedStepNames.remove(executableStep)
		}
	}

	// Target sequence for challenge A: OCPUEFIXHRGWDZABTQJYMNKVSL
	val answerA = executedSteps.reduce { acc, current -> acc + current }
	if (answerA == "OCPUEFIXHRGWDZABTQJYMNKVSL") {
		print("Success! ")
	}
	println("Steps executed in order: $answerA")
}

fun determineNextExecutableStep(stepNames: MutableSet<String>, dependencies: List<Pair<String, String>>, executedSteps: Set<String>): String? {
	var executableStep: String? = null
	// Loop through the step names alphabetically, since this is the execution order in case of a tie.
	for (step in stepNames) {

		if (dependencies.none { it.second == step } ) {
			// First execute steps with no dependencies.
			executableStep = step
			break
		} else if (dependencies.none { !executedSteps.contains(it.first) && it.second == step } ) {
			// We have executed all steps that the current step is dependent on, so we can execute it now.
			executableStep = step
			break
		}
	}
	return executableStep
}
