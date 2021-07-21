package com.example.todo.ui.roster

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.BuildConfig
import com.example.todo.di.AppScope
import com.example.todo.repo.FilterMode
import com.example.todo.repo.FilterMode.ALL
import com.example.settings.PrefsRepository
import com.example.todo.repo.TodoModel
import com.example.todo.repo.TodoRepository
import com.example.todo.report.RosterReport
import com.example.todo.ui.ErrorDialogFragment
import com.example.todo.ui.ErrorScenario
import com.example.todo.ui.ErrorScenario.Import
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

private const val AUTHORITY = BuildConfig.APPLICATION_ID + ".provider"

@HiltViewModel
class RosterViewModel @Inject constructor(
    private val repository: TodoRepository,
    private val report: RosterReport,
    @AppScope val appScope: CoroutineScope,
    @ApplicationContext val context: Context,
    private val prefsRepository: PrefsRepository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _states = MutableStateFlow(RosterViewState())
    val states: StateFlow<RosterViewState> = _states
    private var lastJob: Job? = null
    private val _navEvents = Channel<Nav>(Channel.CONFLATED)
    val navEvents = _navEvents.receiveAsFlow()

    private val _errorEvents = Channel<ErrorScenario>(Channel.CONFLATED)
    val errorEvents = _errorEvents.receiveAsFlow()

    init {
        load(ALL)
    }

    fun load(filterMode: FilterMode) {
        lastJob?.cancel()
        lastJob = viewModelScope.launch {
            repository.items(filterMode).map { RosterViewState(it, filterMode) }
                .collect {
                    _states.value = it
                }
        }
    }

    fun save(model: TodoModel) {
        viewModelScope.launch {
            repository.save(model)
        }
    }

    fun saveReport(doc: Uri) {
        viewModelScope.launch {
            report.generate(_states.value.items, doc)
            _navEvents.trySend(Nav.ViewReport(doc))
        }
    }

    fun shareReport() {
        viewModelScope.launch {
            saveForSharing()
        }
    }

    private suspend fun saveForSharing() {
        withContext(Dispatchers.IO + appScope.coroutineContext) {
            val shared = File(context.cacheDir, "shared").also { it.mkdirs() }
            val reportFile = File(shared, "report.html")
            val doc = FileProvider.getUriForFile(context, AUTHORITY, reportFile)

            report.generate(_states.value.items, doc)
            _navEvents.trySend(Nav.ShareReport(doc))
        }
    }

    fun importItems() {
        viewModelScope.launch {
            try {
                repository.importItems(prefsRepository.loadWebServiceUrl())
            } catch (ex: Exception) {
                Log.e("Todo", "Exception importing items", ex)
                _errorEvents.trySend(Import)
            }
        }
    }

    fun clearImportError() {
        savedStateHandle.remove<ErrorScenario>(ErrorDialogFragment.KEY_RETRY)
    }
}

data class RosterViewState(
    val items: List<TodoModel> = listOf(),
    val filterMode: FilterMode = ALL
)

sealed class Nav {
    data class ViewReport(val doc: Uri) : Nav()
    data class ShareReport(val doc: Uri) : Nav()
}