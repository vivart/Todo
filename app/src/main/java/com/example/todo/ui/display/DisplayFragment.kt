package com.example.todo.ui.display

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.State
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.databinding.TodoDisplayBinding
import com.example.todo.ui.SingleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DisplayFragment : Fragment() {
    private lateinit var binding: TodoDisplayBinding
    private val viewModel: SingleViewModel by viewModels()
    private val args: DisplayFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_display, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit) {
            edit()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = TodoDisplayBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(State.RESUMED) {
                viewModel.states.collect { state ->
                    state.item?.let {
                        binding.apply {
                            completed.visibility = if (it.isCompleted) View.VISIBLE else View.GONE
                            description.text = it.description
                            createdOn.text = DateUtils.getRelativeDateTimeString(
                                requireContext(),
                                it.createdOn.toEpochMilli(),
                                DateUtils.MINUTE_IN_MILLIS,
                                DateUtils.WEEK_IN_MILLIS,
                                0
                            )
                            notes.text = it.notes
                        }
                    }
                }
            }
        }
    }

    fun edit() {
        findNavController().navigate(DisplayFragmentDirections.editModel(args.modelId))
    }
}
