package aoc2018.dao

data class LicenseTreeNode(val header: Pair<Int, Int>) {

	val childNodes = ArrayList<LicenseTreeNode>()

	val metadataEntries = ArrayList<Int>()
}