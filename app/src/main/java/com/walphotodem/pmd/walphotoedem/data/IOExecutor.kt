package com.walphotodem.pmd.walphotoedem.data

import android.os.Handler
import java.util.concurrent.Executor

/**
 */
class IOExecutor : Executor {

    private val mHandler = Handler()

    override fun execute(command: Runnable) {
        mHandler.post(command)
    }

}