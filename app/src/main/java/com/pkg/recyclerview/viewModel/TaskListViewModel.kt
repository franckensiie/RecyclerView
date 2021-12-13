package com.pkg.recyclerview.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkg.recyclerview.data.TasksRepository
import com.pkg.recyclerview.model.Task
import kotlinx.coroutines.launch

class TaskListViewModel: ViewModel() {
    private val repo = TasksRepository();

    private val t = ArrayList<Task>();
    private val _taskList = MutableLiveData<ArrayList<Task>>()
    public val taskList: LiveData<ArrayList<Task>> = _taskList;

    fun refresh() {
        viewModelScope.launch {
            val tasks = repo.loadTasks();
            if (tasks != null) {
                t.removeAll{ e -> true }
                t.addAll(tasks)
                _taskList.value = ArrayList<Task>(t);
            }
        }
    }

    fun updateTask(task: Task, oldTask: Task) {
        viewModelScope.launch {
            val tasksResponse = repo.updateTask(task);
            t.removeAll { e -> e.id == oldTask.id }
            t.add(task)
            _taskList.value = ArrayList<Task>(t);
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch {
            val tasksResponse = repo.createTask(task);
            t.add(task);
            _taskList.value = ArrayList<Task>(t);
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repo.removeTask(task);
            t.removeAll{ e -> e.id == task.id }
            _taskList.value = ArrayList<Task>(t);
        }
    }
}