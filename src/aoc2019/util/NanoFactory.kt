package aoc2019.util

import aoc2019.dao.Chemical

const val FAT_ARROW = "=>"

class NanoFactory(private val chemicalRegistry: MutableMap<String, Chemical> = HashMap()) {

	private val requiredAmountPerChemical = HashMap<Chemical, Int>()
	private var currentRequiredAmount = 0
	private var isProductDefinition = false

	fun processFormula(line: String) {
		val parts = line.split("\\s".toRegex())
		for (part in parts) {
			processPart(part)
		}
	}

	private fun processPart(part: String) {
		when {
			FAT_ARROW == part -> {
				isProductDefinition = true
			}
			currentRequiredAmount == 0 -> {
				currentRequiredAmount = Integer.parseInt(part)
			}
			isProductDefinition -> {
				val product = chemicalRegistry.computeIfAbsent(part) { Chemical(part) }
				product.batchSize = currentRequiredAmount
				product.componentsRequired.putAll(requiredAmountPerChemical)
				requiredAmountPerChemical.keys.forEach { it.requiredInComponents.add(product) }
			}
			else -> {
				val componentName = part.replace(",", "")
				val component = chemicalRegistry.computeIfAbsent(componentName) { Chemical(componentName) }
				requiredAmountPerChemical[component] = currentRequiredAmount
				currentRequiredAmount = 0
			}
		}
	}

}