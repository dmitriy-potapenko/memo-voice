package com.sap.codelab.voiceengine

class SpeechRecognizerInteractor private constructor() : ISpeechRecognizerInteractor {

    private object Holder {
        val INSTANCE = SpeechRecognizerInteractor()
    }

    companion object {
        val instance: SpeechRecognizerInteractor by lazy { Holder.INSTANCE }

        lateinit var voiceRecognizer: IVoiceRecognizer

        fun create(voiceRecognizer: IVoiceRecognizer) {
            this.voiceRecognizer = voiceRecognizer
        }
    }

    override fun startVoiceRecognition(voiceRecognitionListener: IVoiceRecognitionListener) {
        voiceRecognizer.startListening(voiceRecognitionListener)
    }

    override fun stopVoiceRecognition() {
        voiceRecognizer.stopListening()
    }
}