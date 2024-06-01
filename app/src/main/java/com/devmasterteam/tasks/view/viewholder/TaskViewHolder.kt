package com.devmasterteam.tasks.view.viewholder

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.RowTaskListBinding
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.service.model.TaskModel
import java.text.SimpleDateFormat

class TaskViewHolder(private val itemBinding: RowTaskListBinding, val listener: TaskListener) :
    RecyclerView.ViewHolder(itemBinding.root) {

    /**
     * Atribui valores aos elementos de interface e também eventos
     */
    @SuppressLint("SimpleDateFormat")
    fun bindData(task: TaskModel) {

        itemBinding.textDescription.text = task.description
        itemBinding.textPriority.text = task.priorityDescription
        val date = SimpleDateFormat("yyyy-MM-dd").parse(task.dueDate)
        itemBinding.textDueDate.text = date?.let { SimpleDateFormat("dd/MM/yyyy").format(it) }

        if (task.complete)
            itemBinding.imageTask.setImageResource(R.drawable.ic_done)
        else
            itemBinding.imageTask.setImageResource(R.drawable.ic_todo)

        // Eventos
        itemBinding.textDescription.setOnClickListener { listener.onListClick(task.id) }
        itemBinding.imageTask.setOnClickListener {
            if (task.complete)
                listener.onUndoClick(task.id)
            else
                listener.onCompleteClick(task.id)
        }

        itemBinding.textDescription.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.remove_task)
                .setMessage(R.string.text_remove_task)
                .setPositiveButton(R.string.yes) { dialog, which ->
                    listener.onDeleteClick(task.id)
                }
                .setNeutralButton(R.string.cancel, null)
                .show()
            true
        }
    }
}