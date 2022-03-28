package com.sap.codelab.voiceengine

interface IVoiceRecognizer {

    fun startListening(voiceRecognitionListener: IVoiceRecognitionListener)

    fun cancelListening()
}