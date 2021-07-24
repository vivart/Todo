package com.example.todo.repo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.util.UUID

@Entity(tableName = "todos", indices = [Index(value = ["id"])])
data class TodoEntity(
    val description: String,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val createdOn: Instant = Instant.now(),
    val isCompleted: Boolean = false,
    val notes: String = ""
) {
    constructor(model: TodoModel) : this(
        id = model.id,
        description = model.description,
        isCompleted = model.isCompleted,
        notes = model.notes,
        createdOn = model.createdOn
    )

    fun toModel(): TodoModel {
        return TodoModel(
            id = id,
            description = description,
            isCompleted = isCompleted,
            notes = notes,
            createdOn = createdOn
        )
    }

    @Dao
    interface Store {

        @Query("SELECT * FROM todos ORDER BY description")
        fun all(): Flow<List<TodoEntity>>

        @Query("SELECT * FROM todos WHERE id=:modelId")
        fun find(modelId: String?): Flow<TodoEntity?>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun save(vararg entities: TodoEntity)

        @Delete
        suspend fun delete(vararg entities: TodoEntity)

        @Query("SELECT * FROM todos WHERE isCompleted=:isCompleted ORDER BY description")
        fun filtered(isCompleted: Boolean): Flow<List<TodoEntity>>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun importItems(entities: List<TodoEntity>)
    }
}
