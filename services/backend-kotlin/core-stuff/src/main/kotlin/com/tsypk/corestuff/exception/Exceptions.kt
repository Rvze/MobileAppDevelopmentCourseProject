package com.tsypk.corestuff.exception

class NotIphoneIdException(input: String) :
    RuntimeException("Can not extract iphoneFullModel from input=$input")

class NotAirPodsIdException(input : String) :
        RuntimeException("Can not extract airpods full model from input=$input")