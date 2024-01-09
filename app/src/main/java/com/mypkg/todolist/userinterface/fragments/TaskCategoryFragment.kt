package com.mypkg.todolist.userinterface.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mypkg.todolist.userinterface.MainActivity
import com.mypkg.todolist.userinterface.MainActivityViewModel
import com.mypkg.todolist.R
import com.mypkg.todolist.userinterface.adapter.TasksAdapter
import com.mypkg.todolist.databinding.FragmentTaskCategoryBinding
import com.mypkg.todolist.data.model.TaskCategoryInfo
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class TaskCategoryFragment : ParentFragment() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentTaskCategoryBinding
    private val args: TaskCategoryFragmentArgs by navArgs()
    private lateinit var category: String
    @Inject
    @Named("task_category_fragment")
    lateinit var adapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task_category, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        adapter.setOnItemClickListener {
            editTaskInformation(it)
        }
        adapter.setOnTaskStatusChangedListener {
            updateTaskStatus(viewModel, it)
        }
        category = args.categoryString
        initRecyclerView()
        binding.apply {
            text.text = category
        }
        viewModel.getUncompletedTaskOfCategory(category).observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()) binding.noResultAnimationView.visibility = View.VISIBLE
            else binding.noResultAnimationView.visibility = View.GONE
            adapter.differ.submitList(it)
        })
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun editTaskInformation(taskCategoryInfo: TaskCategoryInfo) {
        val action = TaskCategoryFragmentDirections.actionTaskCategoryFragmentToNewTaskFragment(
            taskCategoryInfo
        )
        findNavController().navigate(action)
    }

    private val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            val position = viewHolder.adapterPosition
            val taskInfo = adapter.differ.currentList[position]?.myTaskInfo
            val categoryInfo = adapter.differ.currentList[position]?.myCatInfo?.get(0)
            if (taskInfo != null && categoryInfo != null) {
                deleteTask(viewModel, taskInfo, categoryInfo)
                Snackbar.make(binding.root, "Deleted Successfully", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            viewModel.insertTaskAndCategory(taskInfo, categoryInfo)
                        }
                        show()
                    }
            }
        }
    }
}