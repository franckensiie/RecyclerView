package com.pkg.recyclerview.tasklist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pkg.recyclerview.R
import com.pkg.recyclerview.databinding.FragmentTaskListBinding
import java.util.*


class TaskListFragment : Fragment() {
    //private val taskList = listOf("Task 1", "Task 2", "Task 3")
    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity);
        val myAdapter = TaskListAdapter()
        recyclerView.adapter = myAdapter
        myAdapter.submitList(taskList.toList())

        val button = view.findViewById<FloatingActionButton>(R.id.floatingActionButton);
        button.setOnClickListener {
            taskList.add(Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}"))
            myAdapter.submitList(taskList.toList())
        }
    }
}