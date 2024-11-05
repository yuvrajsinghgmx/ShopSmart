package com.yuvrajsinghgmx.shopsmart.datastore

data class VoiceToTextParserState(
    val spokenText: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null
)
