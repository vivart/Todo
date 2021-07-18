package com.example.todo.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.repo.TodoModel
import com.example.todo.repo.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleViewModel @Inject constructor(
    private val repository: TodoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val modelId: String? = savedStateHandle["modelId"]

    val states = repository.find(modelId)
        .map { SingleModelViewState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SingleModelViewState()
        )

    fun save(model: TodoModel) {
        viewModelScope.launch {
            repository.save(model)
        }
    }

    fun delete(model: TodoModel) {
        viewModelScope.launch {
            repository.delete(model)
        }
    }
}

data class SingleModelViewState(
    val item: TodoModel? = null
)