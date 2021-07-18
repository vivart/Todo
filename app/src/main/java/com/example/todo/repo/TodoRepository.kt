package com.example.todo.repo

import com.example.todo.repo.FilterMode.ALL
import com.example.todo.repo.FilterMode.COMPLETED
import com.example.todo.repo.FilterMode.OUTSTANDING
import com.example.todo.repo.TodoEntity.Store
import com.example.todo.service.TodoRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

enum class FilterMode { ALL, OUTSTANDING, COMPLETED }

@Singleton
class TodoRepository @Inject constructor(
    val store: Store,
    val appScope: CoroutineScope,
    val remoteDataSource: TodoRemoteDataSource
) {
    fun items(filterMode: FilterMode = ALL): Flow<List<TodoModel>> =
        filteredEntities(filterMode).map { all -> all.map { it.toModel() } }

    fun find(id: String?): Flow<TodoModel?> =
        store.find(id).map { it?.toModel() }

    suspend fun save(model: TodoModel) {
        withContext(appScope.coroutineContext) {
            store.save(TodoEntity(model))
        }
    }

    suspend fun delete(model: TodoModel) {
        withContext(appScope.coroutineContext) {
            store.delete(TodoEntity(model))
        }
    }

    suspend fun importItems(url: String) {
        withContext(appScope.coroutineContext) {
            store.importItems(remoteDataSource.load(url).also { print(it) }.map { it.toEntity() })
        }
    }

    private fun filteredEntities(filterMode: FilterMode) = when (filterMode) {
        ALL -> store.all()
        OUTSTANDING -> store.filtered(isCompleted = false)
        COMPLETED -> store.filtered(isCompleted = true)
    }
}