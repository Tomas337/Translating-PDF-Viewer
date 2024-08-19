package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope

suspend fun PointerInputScope.detectAndHandleScroll(
    lazyColumnState: LazyListState,
    setScrollable: (Boolean) -> Unit
) {
    awaitPointerEventScope {
        var previousEvent = PointerEvent(emptyList())
        while (true) {
            val curEvent = awaitPointerEvent(PointerEventPass.Initial)

            if (previousEvent.type == PointerEventType.Move &&
                curEvent.type == PointerEventType.Release
            ) {
                // Make pager scrollable when the end of the page is reached.

                val pointerEvent = previousEvent.changes.first()
                val delta =
                    pointerEvent.position - pointerEvent.previousPosition

                val isDraggingUpwards = delta.y < 0f
                val isDraggingDownwards = delta.y > 0f
                val isAtBottom = !lazyColumnState.canScrollForward
                val isAtTop = !lazyColumnState.canScrollBackward

                val isScrollable = (isAtBottom && isDraggingUpwards) ||
                        (isAtTop && isDraggingDownwards)
                setScrollable(isScrollable)

            } else if (curEvent.type == PointerEventType.Move) {
                // Ensure the user can't change page to the previous one when at the end of the page
                // and can't change page to the next one when at the start of the page.
                // Also allows pager scrolling when the column isn't scrollable.

                val pointerEvent = curEvent.changes.first()
                val delta =
                    pointerEvent.position - pointerEvent.previousPosition

                val isDraggingUpwards = delta.y < 0f
                val isDraggingDownwards = delta.y > 0f
                val isAtBottom = !lazyColumnState.canScrollForward
                val isAtTop = !lazyColumnState.canScrollBackward

                if (isAtBottom && isAtTop) {
                    setScrollable(true)
                } else if ((isAtBottom && isDraggingDownwards) ||
                           (isAtTop && isDraggingUpwards)
                ) {
                    setScrollable(false)
                }
            }
            previousEvent = curEvent
        }
    }
}