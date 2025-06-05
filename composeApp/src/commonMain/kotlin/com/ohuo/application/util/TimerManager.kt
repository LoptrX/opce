package com.ohuo.application.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TimerManager(
    private val coroutineScope: CoroutineScope, // 由平台层传入，用于生命周期管理
    private val interval: Long = 5 // 间隔5秒
) {
    private var refreshJob: Job? = null
    private val mutex = Mutex() // 添加互斥锁
    // 开始定时任务
    fun startPeriodicRefresh(isImmediately:Boolean = false,onRefresh: suspend () -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            mutex.withLock { // 确保同步操作
                refreshJob?.cancelAndJoin() // 等待旧任务完全停止
                refreshJob = launch {
                    if (isImmediately) onRefresh()
                    while (true) {
                        delay(interval * 1000L)
                        try {
                            onRefresh() // 执行刷新操作
                        } catch (e: Exception) {
                            error("TimerManger", e.message ?: "")
                        }
                    }
                }
            }

        }
    }

    // 停止定时任务
    fun stopPeriodicRefresh() {
        info("timer", "stop schedule task")
        refreshJob?.cancel()
        refreshJob = null
    }
}