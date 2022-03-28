package com.sap.codelab.voiceengine

import android.content.Context

class SpeechRecognizerInteractor private constructor() : ISpeechRecognizerInteractor {

    private object Holder {
        val INSTANCE = SpeechRecognizerInteractor()
    }

    companion object {
        val instance: SpeechRecognizerInteractor by lazy { Holder.INSTANCE }

        private lateinit var voiceRecognizer: IVoiceRecognizer

        fun create(context: Context) {
            voiceRecognizer = MemoSimpleVoiceRecognizer(context)
        }
    }

    override fun startVoiceRecognition(voiceRecognitionListener: IVoiceRecognitionListener) {
        voiceRecognizer.startListening(voiceRecognitionListener)
    }

    override fun stopVoiceRecognition() {
        voiceRecognizer.stopListening()
    }
}