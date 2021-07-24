package com.example.todo.ui.roster

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.databinding.TodoRosterBinding
import com.example.todo.repo.FilterMode
import com.example.todo.repo.FilterMode.ALL
import com.example.todo.repo.FilterMode.COMPLETED
import com.example.todo.repo.FilterMode.OUTSTANDING
import com.example.todo.repo.TodoModel
import com.example.todo.ui.ErrorDialogFragment
import com.example.todo.ui.ErrorScenario
import com.example.todo.ui.ErrorScenario.Import
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "ToDo"

@AndroidEntryPoint
class RosterListFragment : Fragment() {

    private val viewModel: RosterViewModel by viewModels()
    private lateinit var binding: TodoRosterBinding
    private val menuMap = mutableMapOf<FilterMode, MenuItem>()
    private val createDoc = registerForActivityResult(ActivityResultContracts.CreateDocument()) {
        viewModel.saveReport(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_roster, menu)
        menuMap.apply {
            put(ALL, menu.findItem(R.id.all))
            put(COMPLETED, menu.findItem(R.id.completed))
            put(OUTSTANDING, menu.findItem(R.id.outstanding))
        }
        menuMap[viewModel.states.value.filterMode]?.isChecked = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                add()
                return true
            }
            R.id.all -> {
                item.isChecked = true
                viewModel.load(ALL)
                return true
            }
            R.id.completed -> {
                item.isChecked = true
                viewModel.load(COMPLETED)
                return true
            }
            R.id.outstanding -> {
                item.isChecked = true
                viewModel.load(OUTSTANDING)
                return true
            }
            R.id.save -> {
                saveReport()
                return true
            }
            R.id.share -> {
                viewModel.shareReport()
                return true
            }
            R.id.importItems -> {
                viewModel.importItems()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = TodoRosterBinding
        .inflate(inflater, container, false).also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RosterAdapter(
            onCheckboxToggle = ::save,
            onRowClick = ::display
        )
        binding.items.apply {
            setAdapter(adapter)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    activity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.states.collect { state ->
                        adapter.submitList(state.items)
                        binding.progressBar.visibility = View.GONE
                        when {
                            state.items.isEmpty() && state.filterMode == ALL -> {
                                binding.empty.visibility = View.VISIBLE
                                binding.empty.setText(R.string.msg_empty)
                            }
                            state.items.isEmpty() -> {
                                binding.empty.visibility = View.VISIBLE
                                binding.empty.setText(R.string.msg_empty_filtered)
                            }
                            else -> binding.empty.visibility = View.GONE
                        }
                        menuMap[state.filterMode]?.isChecked = true
                    }
                }
                launch {
                    viewModel.navEvents.collect { nav ->
                        when (nav) {
                            is Nav.ViewReport -> viewReport(nav.doc)
                            is Nav.ShareReport -> shareReport(nav.doc)
                        }
                    }
                }
                launch {
                    viewModel.errorEvents.collect { error ->
                        when (error) {
                            Import -> handleImportError()
                        }
                    }
                }

                launch {
                    findNavController()
                        .getBackStackEntry(R.id.rosterListFragment)
                        .savedStateHandle
                        .getLiveData<ErrorScenario>(ErrorDialogFragment.KEY_RETRY)
                        .observe(viewLifecycleOwner) { retryScenario ->
                            clearImportError()
                            if (retryScenario == Import) {
                                viewModel.importItems()
                            }
                        }
                }
            }
        }
    }

    private fun handleImportError() {
        findNavController().navigate(
            RosterListFragmentDirections.showError(
                getString(R.string.import_error_title),
                getString(R.string.import_error_message),
                Import
            )
        )
    }

    private fun clearImportError() {
        viewModel.clearImportError()
    }

    private fun saveReport() {
        createDoc.launch("report.html")
    }

    private fun save(model: TodoModel) {
        viewModel.save(model.copy(isCompleted = !model.isCompleted))
    }

    private fun display(model: TodoModel) {
        findNavController().navigate(RosterListFragmentDirections.displayModel(model.id))
    }

    private fun add() {
        findNavController().navigate(RosterListFragmentDirections.createModel(null))
    }

    private fun safeStartActivity(intent: Intent) {
        try {
            startActivity(intent)
        } catch (t: Throwable) {
            Log.e(TAG, "Exception starting $intent", t)
            Toast.makeText(requireActivity(), R.string.oops, Toast.LENGTH_LONG).show()
        }
    }

    private fun viewReport(uri: Uri) {
        safeStartActivity(
            Intent(Intent.ACTION_VIEW, uri)
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        )
    }

    private fun shareReport(doc: Uri) {
        safeStartActivity(
            Intent(Intent.ACTION_SEND)
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .setType("text/html")
                .putExtra(Intent.EXTRA_STREAM, doc)
        )
    }
}
