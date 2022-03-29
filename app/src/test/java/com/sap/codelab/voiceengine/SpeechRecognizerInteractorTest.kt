package com.sap.codelab.voiceengine

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class SpeechRecognizerInteractorTest {

    private val voiceRecognizer: IVoiceRecognizer = mock()
    private lateinit var speechRecognizerInteractor: SpeechRecognizerInteractor

    @Before
    fun setUp() {
        SpeechRecognizerInteractor.create(voiceRecognizer)
        speechRecognizerInteractor = SpeechRecognizerInteractor.instance
    }

    @Test
    fun `test startVoiceRecognition calls start listening`() {
        speechRecognizerInteractor.startVoiceRecognition(mock())
        verify(voiceRecognizer, times(1)).startListening(any())
    }

    @Test
    fun `test stopVoiceRecognition calls start stop listening`() {
        speechRecognizerInteractor.stopVoiceRecognition()
        verify(voiceRecognizer, times(1)).stopListening()
    }
}