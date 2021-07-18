package com.example.todo.ui.roster

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.todo.databinding.TodoRowBinding
import com.example.todo.repo.TodoModel

class RosterAdapter(
    private val onCheckboxToggle: (TodoModel) -> Unit,
    private val onRowClick: (TodoModel) -> Unit
) : ListAdapter<TodoModel, RosterViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RosterViewHolder(
            TodoRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCheckboxToggle,
            onRowClick
        )

    override fun onBindViewHolder(holder: RosterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<TodoModel>() {
        override fun areItemsTheSame(oldItem: TodoModel, newItem: TodoModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TodoModel, newItem: TodoModel) =
            oldItem.isCompleted == newItem.isCompleted
                && oldItem.description == oldItem.description
    }
}