package com.ohuo.application.database.repository

import com.ohuo.application.database.Db
import com.ohuo.application.database.model.ServerApiConfig

fun findById(id: Long): ServerApiConfig? {
    return Db.instance.serverApiConfigQueries.selectOne(id).executeAsOneOrNull()
}

fun getPriority(): ServerApiConfig? {
    return Db.instance.serverApiConfigQueries.selectPriority().executeAsOneOrNull()
}

fun save(serverApiConfig: ServerApiConfig): ServerApiConfig {
    Db.instance.serverApiConfigQueries.save(
        serverApiConfig.serverUrl,
        serverApiConfig.apiKey,
        serverApiConfig.timestamp,
        serverApiConfig.alias,
        serverApiConfig.priority
    )
    val id = Db.instance.serverApiConfigQueries.selectLastInsertRowId().executeAsOne()
    return serverApiConfig.copy(id = id)
}

