package aoc2019.dao

data class OrbitalObject(val name: String) {
	var parent: OrbitalObject? = null
	val children = ArrayList<OrbitalObject>()
}