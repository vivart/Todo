package com.example.todo.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.todo.service.TodoItem
import com.example.todo.service.TodoRemoteDataSource
import com.example.todo.service.TodoService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.equalTo
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ToDoRepositoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val db = TodoDatabase.newTestInstance(context)
    private val remoteDataSource = TodoRemoteDataSource(object : TodoService {
        override suspend fun listTodoItems(url: String): List<TodoItem> {
            return emptyList()
        }
    })

    @Test
    fun canAddItems() = runBlockingTest {
        val underTest = TodoRepository(db.todoStore(), this, remoteDataSource)
        val results = mutableListOf<List<TodoModel>>()

        val itemsJob = launch {
            underTest.items().collect { results.add(it) }
        }

        assertThat(results.size, equalTo(1))
        assertThat(results[0], empty())

        val testModel = TodoModel("test model")

        underTest.save(testModel)

        assertThat(results.size, equalTo(2))
        assertThat(results[1], contains(testModel))
        assertThat(underTest.find(testModel.id).first(), equalTo(testModel))

        itemsJob.cancel()
    }

    @Test
    fun canModifyItems() = runBlockingTest {
        val underTest = TodoRepository(db.todoStore(), this, remoteDataSource)
        val testModel = TodoModel("Test model")
        val replacement = testModel.copy(notes = "This is the replacement")
        val results = mutableListOf<List<TodoModel>>()

        val itemsJob = launch {
            underTest.items().collect { results.add(it) }
        }

        assertThat(results[0], empty())
        underTest.save(testModel)
        assertThat(results[1], contains(testModel))
        underTest.save(replacement)
        assertThat(results[2], contains(replacement))
        itemsJob.cancel()
    }

    @Test
    fun canRemoveItems() = runBlockingTest {
        val underTest = TodoRepository(db.todoStore(), this, remoteDataSource)
        val testModel = TodoModel("Test model")
        val results = mutableListOf<List<TodoModel>>()
        val itemsJob = launch {
            underTest.items().collect { results.add(it) }
        }
        assertThat(results[0], empty())
        underTest.save(testModel)
        assertThat(results[1], contains(testModel))
        underTest.delete(testModel)
        assertThat(results[2], empty())
        itemsJob.cancel()
    }
}
