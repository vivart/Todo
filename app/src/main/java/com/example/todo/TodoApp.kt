package com.example.todo

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todo.di.AppScope
import com.example.todo.repo.ImportWorker
import com.example.todo.repo.PrefsRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG_IMPORT_WORK = "doPeriodicImport"

@HiltAndroidApp
class TodoApp : Application(), Configuration.Provider {

    @Inject lateinit var prefsRepository: PrefsRepository
    @Inject @AppScope lateinit var appScope: CoroutineScope
    @Inject lateinit var workerFactory: HiltWorkerFactory

    private fun scheduleWork() {
        appScope.launch {
            prefsRepository.observeImportChanges().collect {
                if (it) {
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                    val request =
                        PeriodicWorkRequestBuilder<ImportWorker>(15, TimeUnit.MINUTES)
                            .setConstraints(constraints)
                            .addTag(TAG_IMPORT_WORK)
                            .build()

                    WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
                        TAG_IMPORT_WORK,
                        ExistingPeriodicWorkPolicy.REPLACE,
                        request
                    )
                } else {
                    WorkManager.getInstance(applicationContext).cancelAllWorkByTag(TAG_IMPORT_WORK)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        scheduleWork()
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
}