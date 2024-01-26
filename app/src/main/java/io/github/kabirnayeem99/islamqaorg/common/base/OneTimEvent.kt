package io.github.kabirnayeem99.islamqaorg.common.base

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class OneTimeEvent<T> {
    private val channel = MutableSharedFlow<T>(
        extraBufferCapacity = 5,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun sendEvent(event: T) = channel.tryEmit(event)

    fun asFlow(): Flow<T> = channel.asSharedFlow()
}
