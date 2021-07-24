package com.example.todo.ui.roster

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todo.databinding.TodoRowBinding
import com.example.todo.repo.TodoModel

class RosterViewHolder(
    private val binding: TodoRowBinding,
    private val onCheckboxToggle: (TodoModel) -> Unit,
    private val onRowClick: (TodoModel) -> Unit
) : ViewHolder(binding.root) {

    fun bind(model: TodoModel) {
        binding.apply {
            root.setOnClickListener { onRowClick(model) }
            isCompleted.isChecked = model.isCompleted
            isCompleted.setOnCheckedChangeListener { _, _ -> onCheckboxToggle(model) }
            description.text = model.description
        }
    }
}
