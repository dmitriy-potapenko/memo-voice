package com.sap.codelab.voiceengine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import java.util.*

class MemoSimpleVoiceRecognizer(context: Context) : IVoiceRecognizer {

    private val speechRecognizer: SpeechRecognizer =
        SpeechRecognizer.createSpeechRecognizer(context)

    private val speechRecognizerIntent by lazy {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
        }
    }

    private val listener = object : RecognitionListener {

        override fun onReadyForSpeech(params: Bundle?) {
            val texts = params?.getStringArrayList(
                SpeechRecognizer.RESULTS_RECOGNITION
            ) ?: emptyList<String>()
            Log.d("MemoSpeechRecognizer", "onReadyForSpeech: ${texts.joinToString(" ")}")
            voiceRecognitionListener?.onReady()
        }

        override fun onRmsChanged(rmsdB: Float) {
            Log.d("MemoSpeechRecognizer", "onRmsChanged")
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            Log.d("MemoSpeechRecognizer", "onBufferReceived")
        }

        override fun onPartialResults(partialResults: Bundle) {
            val texts = partialResults.getStringArrayList(
                SpeechRecognizer.RESULTS_RECOGNITION
            ) ?: emptyList<String>()
            Log.d("MemoSpeechRecognizer", "onPartialResults: ${texts.joinToString(" ")}")
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            Log.d("MemoSpeechRecognizer", "onEvent")
        }

        override fun onBeginningOfSpeech() {
            Log.d("MemoSpeechRecognizer", "onBeginningOfSpeech")
        }

        override fun onEndOfSpeech() {
            Log.d("MemoSpeechRecognizer", "onEndOfSpeech")
        }

        override fun onError(error: Int) {
            Log.d("MemoSpeechRecognizer", "onError: $error")
            voiceRecognitionListener?.onFailure(error)
        }

        override fun onResults(results: Bundle) {
            val texts = results.getStringArrayList(
                SpeechRecognizer.RESULTS_RECOGNITION
            ) ?: emptyList<String>()
            Log.d("MemoSpeechRecognizer", "onResults: ${texts.joinToString(" ")}")
            voiceRecognitionListener?.onResult(texts.joinToString(" "))
            stopListening()
        }
    }

    private var voiceRecognitionListener: IVoiceRecognitionListener? = null

    init {
        speechRecognizer.setRecognitionListener(listener)
    }

    override fun startListening(voiceRecognitionListener: IVoiceRecognitionListener) {
        this.voiceRecognitionListener = voiceRecognitionListener
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    override fun stopListening() {
        voiceRecognitionListener = null
        speechRecognizer.cancel()
    }
}