package com.tsypk.corestuff.services

import com.tsypk.corestuff.controller.dto.FullRecognitionResult
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.iphone.IphoneMemory
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.models.apple.iphone.IphoneSearchModel
import recognitioncommons.models.apple.macbook.Macbook
import recognitioncommons.models.apple.macbook.MacbookMemory
import recognitioncommons.models.apple.macbook.MacbookModel
import recognitioncommons.models.apple.macbook.MacbookRam
import recognitioncommons.models.country.Country
import recognitioncommons.service.recognition.apple.airpods.AirPodsRecognitionService
import recognitioncommons.service.recognition.apple.iphone.IphoneRecognitionService
import recognitioncommons.service.recognition.apple.macbook.MacbookRecognitionService

@ExtendWith(MockitoExtension::class)
class RecognitionServiceTest {
    @InjectMocks
    private lateinit var recognitionService: RecognitionService

    @Mock
    private lateinit var airPodsRecognitionService: AirPodsRecognitionService

    @Mock
    private lateinit var macbookRecognitionService: MacbookRecognitionService

    @Mock
    private lateinit var iphoneRecognitionService: IphoneRecognitionService

    @Test
    fun recognizeSearchModels() {
        val text = "14 PRO MAX 512 black"
        val searchModel = listOf(
            IphoneSearchModel(
                model = IphoneModel.IPHONE_14_PRO_MAX,
                memory = IphoneMemory.GB_512,
                color = AppleColor.BLACK,
                country = Country.USA
            )
        )
        Mockito.`when`(airPodsRecognitionService.containsAirPodsInLine(text)).thenReturn(false)
        Mockito.`when`(macbookRecognitionService.containsMacbookInLine(text)).thenReturn(false)
        Mockito.`when`(iphoneRecognitionService.recognizeSearchModel(text)).thenReturn(searchModel)
        val searchModelsRecognitionResult = recognitionService.recognizeSearchModels(text)
        assertEquals(searchModelsRecognitionResult.iphones, searchModel)
    }

    @Test
    fun recognize() {
        val text = " Macbook Pro 14 M2 PRO 16 1TB Pro Space Gray США \uD83C\uDDFA\uD83C\uDDF8 - 200000"
        val macbooks = mutableListOf(
            Macbook(
                model = MacbookModel.MACBOOK_PRO_14_M2_PRO,
                ram = MacbookRam.GB_16,
                memory = MacbookMemory.TB_1,
                color = AppleColor.SPACE_GRAY,
                price = 200000,
                country = Country.USA
            )
        )
        val recognizeResult = FullRecognitionResult(
            macbooks = macbooks,
            iphones = mutableListOf(),
            airPods = mutableListOf()
        )
        Mockito.`when`(macbookRecognitionService.containsMacbookInLine(text)).thenReturn(true)
        Mockito.`when`(macbookRecognitionService.recognize(text)).thenReturn(macbooks)
        val fullRecognizeResult = recognitionService.recognize(text)
        assertEquals(fullRecognizeResult, recognizeResult)
    }
}