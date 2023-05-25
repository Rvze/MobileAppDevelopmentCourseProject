package com.tsypk.corestuff.services

import com.tsypk.corestuff.controller.dto.FullRecognitionResult
import com.tsypk.corestuff.controller.dto.SearchModelsRecognitionResult
import org.springframework.stereotype.Service
import recognitioncommons.exception.StaffRecognitionException
import recognitioncommons.exception.staffRecognitionExceptionToHumanReadable
import recognitioncommons.service.recognition.apple.airpods.AirPodsRecognitionService
import recognitioncommons.service.recognition.apple.iphone.IphoneRecognitionService
import recognitioncommons.service.recognition.apple.macbook.MacbookRecognitionService
import recognitioncommons.util.normalization.normalizeText

@Service
class RecognitionService(
    private val airPodsRecognitionService: AirPodsRecognitionService,
    private val macbookRecognitionService: MacbookRecognitionService,
    private val iphoneRecognitionService: IphoneRecognitionService,
) {
    fun recognizeSearchModels(text: String): SearchModelsRecognitionResult {
        val result = SearchModelsRecognitionResult()
        normalizeText(text).forEach {
            try {
                if (airPodsRecognitionService.containsAirPodsInLine(it)) {
                    val recognized = airPodsRecognitionService.recognizeSearchModel(it)
                    result.airPods.addAll(recognized)
                } else if (macbookRecognitionService.containsMacbookInLine(it)) {
                    val recognized = macbookRecognitionService.recognizeSearchModel(it)
                    result.macbooks.addAll(recognized)
                } else {
                    val recognized = iphoneRecognitionService.recognizeSearchModel(it)
                    result.iphones.addAll(recognized)
                }
            } catch (e: StaffRecognitionException) {
                result.errors.add(
                    "$it - ${staffRecognitionExceptionToHumanReadable(e)}"
                )
            }
        }
        return result.copy(
            iphones = result.iphones.toSet().toMutableList(),
            airPods = result.airPods.toSet().toMutableList(),
            macbooks = result.macbooks.toSet().toMutableList(),
            errors = result.errors.toSet().toMutableList()
        )
    }

    fun recognize(text: String): FullRecognitionResult {
        val result = FullRecognitionResult()
        normalizeText(text).forEach {
            try {
                if (airPodsRecognitionService.containsAirPodsInLine(it)) {
                    val recognized = airPodsRecognitionService.recognize(it)
                    result.airPods.addAll(recognized)
                } else if (macbookRecognitionService.containsMacbookInLine(it)) {
                    val recognized = macbookRecognitionService.recognize(it)
                    result.macbooks.addAll(recognized)
                } else {
                    val recognized = iphoneRecognitionService.recognize(it)
                    result.iphones.addAll(recognized)
                }
            } catch (e: StaffRecognitionException) {
                result.errors.add(
                    "$it - ${staffRecognitionExceptionToHumanReadable(e)}"
                )
            }
        }
        return result.copy(
            iphones = result.iphones.toSet().toMutableList(),
            airPods = result.airPods.toSet().toMutableList(),
            macbooks = result.macbooks.toSet().toMutableList(),
            errors = result.errors.toSet().toMutableList()
        )
    }
}