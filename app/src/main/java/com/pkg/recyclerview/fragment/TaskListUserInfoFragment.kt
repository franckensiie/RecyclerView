package com.pkg.recyclerview.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.pkg.recyclerview.*
import com.pkg.recyclerview.databinding.FragmentTaskListBinding
import com.pkg.recyclerview.network.Api
import com.pkg.recyclerview.model.Task
import com.pkg.recyclerview.viewModel.TaskListViewModel
import com.pkg.recyclerview.adapter.TaskListAdapter
import kotlinx.coroutines.launch


class TaskListUserInfoFragment : Fragment() {

    private val myAdapter = TaskListAdapter();
    private val viewModel = TaskListViewModel();

    private val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task
        if (task != null) {
            val oldTask = viewModel.taskList.value?.firstOrNull {it.id == task.id}
            if (oldTask != null) {
                viewModel.updateTask(task, oldTask);
            } else {
                viewModel.createTask(task);
            }
            myAdapter.submitList(viewModel.taskList.value?.toList());
        }
    }

    //private val taskList = listOf("Task 1", "Task 2", "Task 3")
    /*private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )*/
    //private val taskList = mutableListOf<Task>();

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
        viewModel.taskList.observe(viewLifecycleOwner) { newList ->
            myAdapter.submitList(newList);
        }

        //val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val recyclerView = binding.recyclerView;
        recyclerView.layoutManager = LinearLayoutManager(activity);
        //val myAdapter = TaskListAdapter()
        recyclerView.adapter = myAdapter

        viewModel.refresh();

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
            viewModel.deleteTask(task);
        }

        // Edit
        myAdapter.onClickEdit = { task ->
            val intent = Intent(activity, FormActivity::class.java)
            intent.putExtra("taskToEdit", task)
            formLauncher.launch(intent)
        }

        binding.avatarImageView.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            formLauncher.launch(intent)
        }

        binding.deco.setOnClickListener {
            val editor = activity!!.getSharedPreferences("TOKEN_SHARE", Context.MODE_PRIVATE).edit()
            editor.putString("TOKEN", "null")
            editor.apply()
            val intent = Intent(activity, AuthenticationActivity::class.java)
            formLauncher.launch(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        if (Api.TOKEN == "null") {
            val intent = Intent(activity, AuthenticationActivity::class.java)
            formLauncher.launch(intent)
            return;
        }
        binding.avatarImageView.load("https://goo.gl/gEgYUd") {
            transformations(CircleCropTransformation())
        }
        // Ici on ne va pas gérer les cas d'erreur donc on force le crash avec "!!"
        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            val userInfoTextView = binding.userInfoTextView;
            userInfoTextView.text = "${userInfo.firstName} ${userInfo.lastName}"
            var img = if (userInfo.avatar.toString() != "null") {
                userInfo.avatar.toString();
            } else {
                // lol, image par default
                "https://i.pinimg.com/236x/b0/7d/91/b07d916ac09e9c5d505d53cab16c61c0.jpg"
            }
            binding.avatarImageView.load(img) {
                transformations(CircleCropTransformation())
                error(R.drawable.ic_launcher_background)
            }
        }
    }
}
