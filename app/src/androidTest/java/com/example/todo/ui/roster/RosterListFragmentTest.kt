package com.example.todo.ui.roster

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.todo.R
import com.example.todo.repo.TodoModel
import com.example.todo.repo.TodoRepository
import com.example.todo.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RosterListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: TodoRepository
    private val items = listOf(
        TodoModel("this is a test"),
        TodoModel("this is another test"),
        TodoModel("this is... wait for it... yet another test"),
    )

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking { items.forEach { repository.save(it) } }
    }

    @Test
    fun testListContents() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.items)).check(matches(hasChildCount(3)))
    }
}