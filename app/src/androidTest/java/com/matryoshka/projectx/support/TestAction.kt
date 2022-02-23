package com.matryoshka.projectx.support

class TestAction(private val description: String) {
    private var isCalled = false

    fun call() {
        isCalled = true
    }

    fun assertIsCalled() {
        return assert(isCalled) { "$description assert called failed" }
    }
}