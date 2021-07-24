package com.example.todo.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.State
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.databinding.TodoEditBinding
import com.example.todo.repo.TodoModel
import com.example.todo.ui.SingleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditFragment : Fragment() {

    private lateinit var binding: TodoEditBinding
    private val viewModel: SingleViewModel by viewModels()
    private val args: EditFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_edit, menu)
        menu.findItem(R.id.delete).isVisible = args.modelId != null
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                save()
                true
            }
            R.id.delete -> {
                delete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = TodoEditBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(State.RESUMED) {
                viewModel.states.collect { state ->
                    state.item?.let {
                        binding.apply {
                            isCompleted.isChecked = it.isCompleted
                            description.setText(it.description)
                            notes.setText(it.notes)
                        }
                    }
                }
            }
        }
    }

    private fun save() {
        val model = viewModel.states.value.item
        val edited = model?.copy(
            description = binding.description.text.toString(),
            isCompleted = binding.isCompleted.isChecked,
            notes = binding.notes.text.toString()
        ) ?: TodoModel(
            description = binding.description.text.toString(),
            isCompleted = binding.isCompleted.isChecked,
            notes = binding.notes.text.toString()
        )
        viewModel.save(edited)
        navToDisplay()
    }

    private fun delete() {
        val model = viewModel.states.value.item
        model?.let { viewModel.delete(it) }
        navToList()
    }

    private fun navToDisplay() {
        hideKeyboard()
        findNavController().popBackStack()
    }

    private fun navToList() {
        hideKeyboard()
        findNavController().popBackStack(R.id.rosterListFragment, false)
    }

    private fun hideKeyboard() {
        view?.let {
            val imm = context?.getSystemService<InputMethodManager>()
            imm?.hideSoftInputFromWindow(
                it.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}
