package com.tsypk.corestuff.controller.dto

import recognitioncommons.models.apple.airpods.AirPodsSearchModel
import recognitioncommons.models.apple.iphone.IphoneSearchModel
import recognitioncommons.models.apple.macbook.MacbookSearchModel

data class SearchModelsRecognitionResult(
    val iphones: MutableList<IphoneSearchModel> = mutableListOf(),
    val airPods: MutableList<AirPodsSearchModel> = mutableListOf(),
    val macbooks: MutableList<MacbookSearchModel> = mutableListOf(),
    val errors: MutableList<String> = mutableListOf(),
) {
    fun allEmpty(): Boolean {
        return totalRecognized() == 0
    }

    fun withoutErrors(): Boolean {
        return errors.isEmpty()
    }

    fun errorsToString(): String {
        val builder = StringBuilder()
        this.errors.forEach {
            builder.append(it)
            builder.append("\n")
        }
        return builder.toString()
    }

    fun totalRecognized(): Int {
        return iphones.size + airPods.size + macbooks.size
    }
}