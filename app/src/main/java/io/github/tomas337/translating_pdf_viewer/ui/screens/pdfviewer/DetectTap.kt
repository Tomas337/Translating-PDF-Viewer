package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.changedToUp

suspend fun PointerInputScope.detectTap(
    pass: PointerEventPass = PointerEventPass.Main,
    callback: () -> Unit,
) {
    awaitPointerEventScope {
        while (true) {
            val down = awaitFirstDown(pass = pass)
            down.consume()
            val up = awaitPointerEvent(pass = pass)

            val isPress = up.changes.any { it.changedToUp() }
            val isShort = up.changes.any {
                (it.uptimeMillis - it.previousUptimeMillis) <= 300
            }

            if (isPress) {
                up.changes.forEach { it.consume() }
            }
            if (isPress && isShort) {
                callback()
            }
        }
    }
}
