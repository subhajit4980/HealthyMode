package com.example.HealthyMode.UI.Task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.HealthyMode.Adapter.Adapter_todo
import com.example.HealthyMode.TodoDatabase.Todo
import com.example.HealthyMode.UI.Task.ViewModel.TodoModel
import com.example.HealthyMode.databinding.FragmentTaskListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskList : Fragment(), Adapter_todo.OnItemClickListener {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private lateinit var todoAdapter: Adapter_todo
    private val ViewModel: TodoModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoAdapter= Adapter_todo(this)
        binding.apply {
            rev.apply {
                adapter = todoAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = todoAdapter.currentList[viewHolder.adapterPosition]
                    ViewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(rev)
        }
        observe()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            ViewModel.tasksEvent.collect { event ->
                when (event) {
                    is TodoModel.TasksEvent.ShowUndoDeleteTaskMessage -> {

                        val snackbar=Snackbar.make(requireView(), "Plan deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                ViewModel.onUndoDeleteClick(event.task)
                            }
                        val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
                        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, 134)
                        snackbar.view.layoutParams = params
                            snackbar.show()
                    }
                }
            }
        }
    }

    private fun observe() {
        ViewModel.allplans.observe(viewLifecycleOwner) { plans ->
            plans?.let {
                if (it.isNotEmpty()) {
                    binding.noplan.visibility = View.GONE
                    binding.rev.visibility = View.VISIBLE
                    todoAdapter.submitList(it)
                } else {
                    binding.noplan.visibility = View.VISIBLE
                    binding.rev.visibility = View.GONE
                }
            }
            if(plans==null)
            {
                binding.noplan.visibility = View.VISIBLE
                binding.rev.visibility = View.GONE
            }
        }
    }
    override fun onCheckBoxClick(task: Todo, isChecked: Boolean) {
        ViewModel.onTaskCheckedChanged(task, isChecked)
    }
}