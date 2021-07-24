package com.example.todo.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.todo.repo.TodoModel
import com.example.todo.repo.TodoRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SingleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testModel = TodoModel("this is a test")
    private val repository = mockk<TodoRepository>(relaxed = true) {
        every { find(testModel.id) } returns flowOf(testModel)
    }
    private val savedStateHandle = mockk<SavedStateHandle> {
        every { get<String>("modelId") } returns testModel.id
    }
    private lateinit var viewModel: SingleViewModel

    @Before
    fun setUp() {
        viewModel = SingleViewModel(repository, savedStateHandle)
    }

    @Test
    fun `initial state`() = mainDispatcherRule.dispatcher.runBlockingTest {
        val state = viewModel.states.first()
        assertThat(state.item?.id, equalTo(testModel.id))
    }

    @Test
    fun `actions pass through to repo`() = mainDispatcherRule.dispatcher.runBlockingTest {
        val replacement = testModel.copy("whatevs")

        viewModel.save(replacement)

        coVerify { repository.save(replacement) }

        viewModel.delete(replacement)

        coVerify { repository.delete(replacement) }
    }
}
