package com.sap.codelab.voiceengine

interface IVoiceRecognitionListener {

    fun onReady()

    fun onResult(result: String)

    fun onFailure(error: Int)
}