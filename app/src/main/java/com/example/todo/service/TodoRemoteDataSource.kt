package com.example.todo.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRemoteDataSource @Inject constructor(val service: TodoService) {
    suspend fun load(url: String) = withContext(Dispatchers.IO) {
        service.listTodoItems(url)
    }
}