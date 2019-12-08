package aoc2018.solutions

import aoc2018.dao.LicenseTreeNode
import util.FileConstants
import java.io.File
import java.util.*

fun main() {

	challengeA()
	challengeB()
}

private fun challengeB() {

	val unstructuredInput = readInput()
	val rootNode = buildTreeNode(unstructuredInput)
	val totalValueRootNode = calculateNodeValue(rootNode)

	println("Total value of the root node: $totalValueRootNode")
}

fun calculateNodeValue(node: LicenseTreeNode): Int {
	var totalValue = 0

	if (node.childNodes.size == 0) {
		// "If a node has no child nodes, its value is the sum of its metadata entries."
		totalValue += node.metadataEntries.sum()
	} else {
		// "If a node does have child nodes, the metadata entries become indexes which refer to those child nodes."
		for (metadata in node.metadataEntries) {
			// Convert the metadata one-based index to a zero-based one.
			val index = metadata - 1
			if (index >= 0 && index < node.childNodes.size) {
				// This metadata reference points to an existing child. Add that child's value to the total value
				// of the current node.
				totalValue += calculateNodeValue(node.childNodes[index])
			}
			// If the index is out of bounds, the value for this metadata reference is zero - so add nothing to total.
		}
	}
	return totalValue
}

private fun challengeA() {

	val unstructuredInput = readInput()
	val rootNode = buildTreeNode(unstructuredInput)
	val totalMetadata = sumMetadata(rootNode)

	println("Total sum of all metadata: $totalMetadata")
}

fun sumMetadata(node: LicenseTreeNode): Int {
	var totalMetadata = node.metadataEntries.sum()
	for (child in node.childNodes) {
		totalMetadata += sumMetadata(child)
	}
	return totalMetadata
}

fun buildTreeNode(unstructuredInput: Queue<Int>): LicenseTreeNode {
	val qtyChildNodes = unstructuredInput.poll() ?: throw IllegalArgumentException("Input is not structured correctly!")
	val qtyMetadata = unstructuredInput.poll() ?: throw IllegalArgumentException("Input is not structured correctly!")
	val treeNode = LicenseTreeNode(Pair(qtyChildNodes, qtyMetadata))

	// First, build the specified number of child nodes.
	for (child in 0 until qtyChildNodes) {
		treeNode.childNodes.add(buildTreeNode(unstructuredInput))
	}
	// Add the specified quantity of metadata.
	for (meta in 0 until qtyMetadata) {
		val metadata = unstructuredInput.poll() ?: throw IllegalArgumentException("Input is not structured correctly!")
		treeNode.metadataEntries.add(metadata)
	}

	return treeNode
}

private fun readInput(): Queue<Int> {
	val inputFile = FileConstants.INPUT_DIR_2018 + "inputDay8.txt"
	val inputInts = ArrayDeque<Int>()
	File(inputFile).forEachLine { line ->
		val splitInts = line.split("\\s".toRegex())
		splitInts.forEach {
			inputInts.add(Integer.parseInt(it))
		}
	}
	return inputInts
}