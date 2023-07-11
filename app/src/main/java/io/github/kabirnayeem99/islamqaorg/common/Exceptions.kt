package io.github.kabirnayeem99.islamqaorg.common

class EmptyCacheException : Exception() {
    override val message: String
        get() = "No cache found"
}

class NoNetworkException : Exception()