package io.github.kabirnayeem99.islamqaorg.common.base

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class OneTimeEvent<T> {
    private val channel = Channel<T>()

    fun sendEvent(event: T) = channel.trySend(event)

    fun asFlow(): Flow<T> = channel.receiveAsFlow()
}
