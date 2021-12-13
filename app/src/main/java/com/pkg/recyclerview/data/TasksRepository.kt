package com.pkg.recyclerview.data

import com.pkg.recyclerview.network.Api
import com.pkg.recyclerview.model.Task

class TasksRepository {
    private val webService = Api.tasksWebService

    suspend fun loadTasks(): List<Task>? {
        val response = webService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun removeTask(task: Task) {
        webService.delete(task.id);
    }
    suspend fun createTask(task: Task): Task? {
        val response = webService.create(task);
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun updateTask(task: Task): Task? {
        val response = webService.update(task);
        return if (response.isSuccessful) response.body() else null
    }
}
