package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService

class TaskRepository(context: Context) : BaseRepository(context) {

    // Instancia do TaskService com o serviço do Retrofit
    private val remote = RetrofitClient.getService(TaskService::class.java)

    // Listar todas as tarefas cadastradas
    fun list(listener: APIListener<List<TaskModel>>) {
        if (!isConnectionAvaliable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.list(), listener)
    }

    // Listar as tarefas que se expiram nos proximos dias
    fun listNext(listener: APIListener<List<TaskModel>>) {
        if (!isConnectionAvaliable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.listNext(), listener)
    }

    // Listar as tarefas expiradas
    fun listOverduo(listener: APIListener<List<TaskModel>>) {
        if (!isConnectionAvaliable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.listOverdue(), listener)
    }

    // Criar tarefa
    fun create(task: TaskModel, listener: APIListener<Boolean>) {
        if (!isConnectionAvaliable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.create(task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, listener)
    }

    // Atualizar tarefa
    fun update(task: TaskModel, listener: APIListener<Boolean>) {
        if (!isConnectionAvaliable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call =
            remote.update(task.id, task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, listener)
    }

    // Deletar tarefa
    fun delete(id: Int, listener: APIListener<Boolean>) {
        if (!isConnectionAvaliable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.delete(id), listener)
    }

    // Marcar tarefa como completa
    fun complete(id: Int, listener: APIListener<Boolean>) {
        if (!isConnectionAvaliable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.complete(id), listener)
    }

    // Marcar tarefa como incompleta
    fun undo(id: Int, listener: APIListener<Boolean>) {
        if (!isConnectionAvaliable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.undo(id), listener)
    }

    // Buscar tarefas filtradas
    fun load(id: Int, listener: APIListener<TaskModel>) {
        if (!isConnectionAvaliable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.load(id), listener)
    }
}