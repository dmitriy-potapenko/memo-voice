package com.sap.codelab.repository

import android.app.Application
import com.sap.codelab.voiceengine.MemoSimpleVoiceRecognizer
import com.sap.codelab.voiceengine.SpeechRecognizerInteractor

/**
 * Extension of the Android Application class.
 */
internal class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.create(this)
        SpeechRecognizerInteractor.create(MemoSimpleVoiceRecognizer(this))
    }
}