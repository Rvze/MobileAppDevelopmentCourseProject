package com.tsypk.corestuff

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling
import recognitioncommons.config.RecognitionLibraryConfiguration

@SpringBootApplication
@Import(RecognitionLibraryConfiguration::class)
@EnableScheduling
class CoreStuffApplication

fun main(args: Array<String>) {
    runApplication<CoreStuffApplication>(*args)
}
