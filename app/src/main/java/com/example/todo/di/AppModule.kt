package com.example.todo.di

import android.content.Context
import android.text.format.DateUtils
import com.example.todo.repo.TodoDatabase
import com.example.todo.repo.TodoEntity.Store
import com.example.todo.repo.TodoRepository
import com.example.todo.service.InstantAdapter
import com.example.todo.service.TodoRemoteDataSource
import com.example.todo.service.TodoService
import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Helper
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providesTodoRepository(
        store: Store,
        @AppScope coroutineScope: CoroutineScope,
        remoteDataSource: TodoRemoteDataSource
    ) =
        TodoRepository(store, coroutineScope, remoteDataSource)

    @Singleton
    @Provides
    fun providesTodoDatabase(@ApplicationContext context: Context) =
        TodoDatabase.newInstance(context)

    @Singleton
    @Provides
    fun providesStore(db: TodoDatabase) = db.todoStore()

    @Provides
    @AppScope
    fun providesCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun providesHandleBars(@ApplicationContext context: Context): Handlebars = Handlebars().apply {
        registerHelper(
            "dateFormat",
            Helper<Instant> { value, _ ->
                DateUtils.getRelativeDateTimeString(
                    context,
                    value.toEpochMilli(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.WEEK_IN_MILLIS, 0
                )
            }
        )
    }

    @Singleton
    @Provides
    fun providesTodoService(): TodoService {
        val gson = GsonBuilder()
            .registerTypeAdapter(Instant::class.java, InstantAdapter())
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.npoint.io/080fa016b3e55e91e212/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(TodoService::class.java)
    }
}
