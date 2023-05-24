package com.tsypk.corestuff.exception

abstract class BusinessException(
    val errorCode: String,
    val errorMsg: String,
) : RuntimeException(errorMsg)


class NotIphoneIdException(input: String) :
    RuntimeException("Can not extract iphoneFullModel from input=$input")

class NotAirPodsIdException(input : String) :
        RuntimeException("Can not extract airpods full model from input=$input")

class RecognitionException(errorMsg: String) :
    BusinessException(
        errorCode = "RECOGNITION_ERROR",
        errorMsg = "$errorMsg\nПожалуйста, следуйте формату, указанному в /help",
    )