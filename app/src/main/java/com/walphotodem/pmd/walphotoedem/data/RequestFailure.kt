package com.walphotodem.pmd.walphotoedem.data

/**
 * Data model for saving failed requests for user retry
 */
class RequestFailure(val retryable: Retryable, val errorMsg: String?)

interface Retryable {
    fun retry()
}
