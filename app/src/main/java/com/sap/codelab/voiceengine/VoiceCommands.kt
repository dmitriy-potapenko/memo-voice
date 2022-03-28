package com.sap.codelab.voiceengine

/**
 * Supported commands for recognizer
 */
enum class VoiceCommands(val textRepresentation: String) {
    SHOW_ALL("show all"),
    SHOW_OPEN("show open"),
    CREATE_MEMO("create memo"),
    UNRECOGNIZED("")
}