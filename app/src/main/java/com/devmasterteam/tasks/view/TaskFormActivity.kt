package com.devmasterteam.tasks.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var listPriority: List<PriorityModel> = mutableListOf()
    private var taskIdentification = TaskConstants.FILTER.ALL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this)[TaskFormViewModel::class.java]
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        // Permissão de click dos botões
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)

        // Busca do banco de dados a lista de prioridades das tasks
        viewModel.loadPriorities()

        loadDataFromActivity()

        observe()

        // Layout
        setContentView(binding.root)
    }

    // Eventos de click dos botões
    override fun onClick(v: View) {
        if (v.id == R.id.button_date)
            handleDate()
        else if (v.id == R.id.button_save)
            handleSave()
    }

    // Altera o conteúdo do botão para a data selecionada
    override fun onDateSet(v: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dueDate = dateFormat.format(calendar.time)
        binding.buttonDate.text = dueDate
    }

    /* Responsável por receber da AllTaskFragment o id que
     * busca as tarefas filtradas da API
     */
    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if (bundle != null) {
            taskIdentification = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.load(taskIdentification)
        }
    }

    // Responsável por abrir o DatePickDialog
    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, day).show()
    }

    // Respónsável por salvar a task
    private fun handleSave() {
        val task = TaskModel().apply {
            this.id = taskIdentification
            val index = binding.spinnerPriority.selectedItemPosition
            this.priorityId = listPriority[index].id
            this.description = binding.editDescription.text.toString()
            this.dueDate = binding.buttonDate.text.toString()
            this.complete = binding.checkComplete.isChecked
        }
        viewModel.save(task)
    }

    // Responsável por simplificar a criação de toasts
    private fun toast(str: String) {
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

    /* Recebe o id da prioridade e retorna o index
     * de acordo com a posição da lista de prioridades
     */
    private fun getIndex(priorityId: Int): Int {
        var index = 0
        for (l in listPriority) {
            if (l.id == priorityId) {
                break
            }
            index++
        }
        return index
    }

    // Observadores da viewModel
    @SuppressLint("SimpleDateFormat")
    private fun observe() {

        // Adiciona as descrições para a lista e passa para o adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.priorityList.collect { value ->
                    listPriority = value
                    val list = mutableListOf<String>()
                    for (p in value) {
                        list.add(p.description)
                    }
                    val adapter = ArrayAdapter(
                        applicationContext,
                        android.R.layout.simple_spinner_dropdown_item,
                        list
                    )
                    binding.spinnerPriority.adapter = adapter
                }
            }
        }

        // Criação ou atualização de tasks
        viewModel.taskSave.observe(this) {
            if (it.status()) {
                if (taskIdentification == 0)
                    toast(getString(R.string.task_created))
                else
                    toast(getString(R.string.task_updated))
                finish()
            } else
                toast(it.message())
        }

        // Recebe a task do load e adiciona seus valores nos respectivos campos
        viewModel.task.observe(this) {
            binding.editDescription.setText(it.description)
            binding.checkComplete.isChecked = it.complete
            binding.spinnerPriority.setSelection(getIndex(it.priorityId))
            val date = SimpleDateFormat("yyyy-MM-dd").parse(it.dueDate)
            binding.buttonDate.text = SimpleDateFormat("dd/MM/yyyy").format(date)
        }

        // Tratamento de erro do load
        viewModel.taskLoad.observe(this) {
            if (!it.status()) {
                toast(it.message())
                finish()
            }
        }
    }
}