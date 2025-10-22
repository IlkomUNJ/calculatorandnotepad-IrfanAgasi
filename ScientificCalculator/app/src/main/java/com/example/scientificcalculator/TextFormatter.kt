package com.example.scientificcalculator

import androidx.compose.runtime.*

class TextFormatter {
    var isBold by mutableStateOf(false)
    var isItalic by mutableStateOf(false)
    var isUnderlined by mutableStateOf(false)
    var textSize by mutableStateOf(16f)

    fun toggleBold() {
        isBold = !isBold
    }

    fun toggleItalic() {
        isItalic = !isItalic
    }

    fun toggleUnderline() {
        isUnderlined = !isUnderlined
    }

    fun increaseTextSize() {
        textSize += 2
    }

    fun decreaseTextSize() {
        if (textSize > 8) textSize -= 2
    }
}
