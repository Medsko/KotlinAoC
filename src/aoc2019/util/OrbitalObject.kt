package aoc2019.util

data class OrbitalObject(val name: String) {
	var parent: OrbitalObject? = null
	val children = ArrayList<OrbitalObject>()
}