package com.ohuo.application.database.repository

import com.ohuo.application.database.Db
import com.ohuo.application.database.model.ServerApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

private val queries = Db.instance.serverApiConfigQueries
suspend fun findById(id: Long): ServerApiConfig? {
    return withContext(Dispatchers.IO) { queries.selectOne(id).executeAsOneOrNull() }
}

suspend fun getPriority(): ServerApiConfig? {
    return withContext(Dispatchers.IO) { queries.selectPriority().executeAsOneOrNull() }
}

suspend fun save(entity: ServerApiConfig): ServerApiConfig {
    return withContext(Dispatchers.IO) {
        if (entity.id == -1L) {
            queries.save(
                entity.serverUrl,
                entity.apiKey,
                entity.time,
                entity.alias,
                entity.priority
            )
            entity.copy(id = queries.selectLastInsertRowId().executeAsOne())
        } else {
            queries.update(
                entity.id,
                entity.serverUrl,
                entity.apiKey,
                entity.time,
                entity.alias,
                entity.priority
            )
            entity
        }
    }
}

