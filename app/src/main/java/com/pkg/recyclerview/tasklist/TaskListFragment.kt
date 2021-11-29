package com.pkg.recyclerview.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pkg.recyclerview.form.FormActivity
import com.pkg.recyclerview.databinding.FragmentTaskListBinding


class TaskListFragment : Fragment() {

    private val myAdapter = TaskListAdapter();

    private val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task
        if (task != null) {
            val oldTask = taskList.firstOrNull {it.id == task.id}
            if (oldTask != null) taskList.removeAll { t -> t.id == oldTask.id }
            taskList.add(task)
            myAdapter.submitList(taskList.toList());
        }
    }

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
        // val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val recyclerView = binding.recyclerView;
        recyclerView.layoutManager = LinearLayoutManager(activity);
        //val myAdapter = TaskListAdapter()
        recyclerView.adapter = myAdapter
        myAdapter.submitList(taskList.toList())

        // val button = view.findViewById<FloatingActionButton>(R.id.floatingActionButton);
        val button = binding.floatingActionButton;
        button.setOnClickListener {
            //taskList.add(Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}"))
            //myAdapter.submitList(taskList.toList())
            // Intent
            val intent = Intent(activity, FormActivity::class.java)
            formLauncher.launch(intent)
        }

        // Delete
        myAdapter.onCLickDelete = { task ->
            taskList.removeAll { t -> t.id == task.id }
            myAdapter.submitList(taskList.toList())
        }

        // Edit
        myAdapter.onClickEdit = { task ->
            val intent = Intent(activity, FormActivity::class.java)
            intent.putExtra("taskToEdit", task)
            formLauncher.launch(intent)
        }
    }
}