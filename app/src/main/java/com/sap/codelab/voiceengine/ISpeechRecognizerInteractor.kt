package com.sap.codelab.voiceengine

interface ISpeechRecognizerInteractor {

    fun startVoiceRecognition(voiceRecognitionListener: IVoiceRecognitionListener)

    fun stopVoiceRecognition()
}