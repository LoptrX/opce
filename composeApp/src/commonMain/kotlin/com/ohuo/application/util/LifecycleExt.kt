package com.ohuo.application.util

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * Extension property to create a CoroutineScope tied to a Lifecycle.
 * The scope will be cancelled when the lifecycle is destroyed.
 */
// 使用静态Map存储Lifecycle实例与CoroutineScope的映射关系
private val scopeMap = mutableMapOf<Lifecycle, CoroutineScope>()

val Lifecycle.scope: CoroutineScope
    get() {
        // 从Map中获取已存在的CoroutineScope，如果不存在则创建新的
        return scopeMap.getOrPut(this) {
            val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
            // 在生命周期销毁时取消CoroutineScope并从Map中移除
            doOnDestroy { 
                scope.cancel() 
                scopeMap.remove(this)
            }
            scope
        }
    }