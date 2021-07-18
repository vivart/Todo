package com.example.todo.di

import android.content.Context
import android.text.format.DateUtils
import androidx.test.platform.app.InstrumentationRegistry
import com.example.todo.repo.TodoDatabase
import com.example.todo.repo.TodoEntity.Store
import com.example.todo.repo.TodoRepository
import com.example.todo.service.TodoItem
import com.example.todo.service.TodoRemoteDataSource
import com.example.todo.service.TodoService
import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Helper
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.time.Instant
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
@Module
object TestAppModule {

    @Singleton
    @Provides
    fun providesTodoRepository(store: Store, @AppScope coroutineScope: CoroutineScope) =
        TodoRepository(store, coroutineScope, TodoRemoteDataSource(object : TodoService {
            override suspend fun listTodoItems(url: String): List<TodoItem> {
                return emptyList()
            }
        }))

    @Singleton
    @Provides
    fun providesTodoDatabase() =
        TodoDatabase.newTestInstance(InstrumentationRegistry.getInstrumentation().targetContext)

    @Singleton
    @Provides
    fun providesStore(db: TodoDatabase) = db.todoStore()

    @Provides
    @AppScope
    fun providesCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun providesHandleBars(@ApplicationContext context: Context): Handlebars = Handlebars().apply {
        registerHelper("dateFormat", Helper<Instant> { value, _ ->
            DateUtils.getRelativeDateTimeString(
                context,
                value.toEpochMilli(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS, 0
            )
        })
    }
}