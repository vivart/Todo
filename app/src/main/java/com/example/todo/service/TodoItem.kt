package com.example.todo.service

import com.example.todo.repo.TodoEntity
import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.Instant
import java.time.format.DateTimeFormatter

data class TodoItem(
    val description: String,
    val id: String,
    val completed: Boolean,
    val notes: String,
    @SerializedName("created_on")
    val createdOn: Instant
) {
    fun toEntity(): TodoEntity {
        return TodoEntity(
            id = id,
            description = description,
            isCompleted = completed,
            notes = notes,
            createdOn = createdOn
        )
    }
}

private val FORMATTER = DateTimeFormatter.ISO_INSTANT

class InstantAdapter : TypeAdapter<Instant>() {
    override fun write(writer: JsonWriter?, value: Instant?) {
        writer?.value(FORMATTER.format(value))
    }

    override fun read(reader: JsonReader?): Instant {
        return FORMATTER.parse(reader?.nextString(), Instant::from)
    }
}
