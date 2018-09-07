package com.walphotodem.pmd.walphotoedem.data

/**
 */
class RequestFailure(val retryable: Retryable, val errorMsg: String?)

interface Retryable {
    fun retry()
}
