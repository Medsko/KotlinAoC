package aoc2018.solutions

import util.FileConstants
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() {
	
	val inputFile = FileConstants.INPUT_DIR + "2018\\inputDay4.txt"
	// Challenge A: 1) find the guard that is asleep for the most minutes
			// 2) find the minute that guard is asleep the most
			// 3) multiply it by the guard's id
	
	// Make a Map with the dates as keys. Order by the dates.
	val schedule = linkedMapOf<LocalDateTime, String>()
	
	File(inputFile).forEachLine {
		val dateString = it.substring(it.indexOf("[") + 1, it.indexOf("]"))
		val actionString = it.substring(it.indexOf("]") + 1)
		val dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
		schedule[dateTime] = actionString	// Kotlin variant of 'Map.put()'
	}
	
	val sortedSchedule = schedule.toSortedMap()
	
	for (key in sortedSchedule.keys) {
		println("" + key + " " + sortedSchedule.get(key))
	}
	
	val guards = hashMapOf<Int, Array<Int>>()
	
	var currentGuard = Array(0) { 0 } // Lambda (the '{ 0 }' part) outside of parentheses.
	var currentGuardId = 0
	val guardIdRegex = "#\\d{1,5}".toRegex()
	var startSleep = 0
	
	for (startAction in sortedSchedule.keys) {
		
//		val currentLine = sortedSchedule.get(startAction)
//		if (currentLine == null) break
		// This does the exact same thing as the two lines above.
		val currentLine = sortedSchedule[startAction] ?: break
		

		// The following shit code block should be whittled down to something concise.
		val guardId = guardIdRegex.find(currentLine)?.value?.substring(1)?.toInt()
		if (guardId != null) {
			// This is the start of a new guard's shift.
			val newCurrentGuard = guards[guardId]
			if (newCurrentGuard != null) {
				currentGuard = newCurrentGuard
			} else {
				currentGuard = Array(60) { 0 }
				guards[guardId] = currentGuard
			}
			currentGuardId = guardId
			continue
		}
		if (currentGuardId == 137) println(startAction.toString() + currentLine)
		
		if (currentLine.contains("asleep")) {
			startSleep = startAction.minute 	// This does the same...
		} else if (currentLine.contains("wakes")) {
			val startWake = startAction.getMinute()	// ...as this.
			
			for (i in startSleep until startWake)
				currentGuard[i]++
		}
	}
	
	for (guardId in guards.keys) {
		println("Guard " + guardId + " slept for " + guards.get(guardId)?.sum() + " minutes.")
	}
	
	val sleepiestGuard = guards.maxBy { it.value.sum() } // ?: return -- This would eliminate the need of next line
	
	if (sleepiestGuard == null) return

	val sleepiestMinute = sleepiestGuard.value.indices.maxBy { sleepiestGuard.value[it] }
	
	// Log answer to challenge A to console. Target: 84636
	if (sleepiestMinute != null) {
		println("Sleepiest guard: " + sleepiestGuard.key + ", sleepiest minute of that guard: " + sleepiestMinute)
		println("Sleepiest minute of the sleepiest guard multiplied by that guard's id: " + sleepiestGuard.key * sleepiestMinute)
	} else {
		println("Sleepiest guard could not be determined!")
	}
	
	// For challenge B, find the guard that is most frequently asleep on the same minute. For the answer, multiply by that guard's id.
	var totalSleepiestMinute = 0
	var sleepiestGuardId = 0
	var sleepiestMinuteOfGuard = 0
	for (guard in guards) {
		val sleepyIndex = guard.value.indices.maxBy { guard.value[it] }
		if (sleepyIndex != null && guard.value[sleepyIndex] > totalSleepiestMinute) {
			totalSleepiestMinute = guard.value[sleepyIndex]
			sleepiestGuardId = guard.key
			sleepiestMinuteOfGuard = sleepyIndex
		}
	}
	// GuardId target = 1871, minute most asleep = 49
	println("Sleepiest guard: $sleepiestGuardId, sleepiest minute of that guard: $sleepiestMinuteOfGuard")
	// Log answer to challenge B to console. Target: 91679
	println("Sleepiest minute of most consistently sleepy guard: " + sleepiestGuardId * sleepiestMinuteOfGuard)	
	
}