package com.example.todo.repo

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.settings.PrefsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ImportWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val prefsRepository: PrefsRepository,
    private val repository: TodoRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork() = try {
        repository.importItems(prefsRepository.loadWebServiceUrl())
        Result.success()
    } catch (ex: Exception) {
        Log.e("ToDo", "Exception importing items in doWork()", ex)
        Result.failure()
    }
}