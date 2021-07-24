package com.example.todo.service

import retrofit2.http.GET
import retrofit2.http.Url

interface TodoService {
    @GET
    suspend fun listTodoItems(@Url url: String): List<TodoItem>
}
