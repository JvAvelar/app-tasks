package com.devmasterteam.tasks.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityRegisterBinding
import com.devmasterteam.tasks.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        // Esconder a barra da tela
        supportActionBar?.hide()

        // Eventos
        binding.buttonSave.setOnClickListener(this)

        observe()

        // Layout
        setContentView(binding.root)
    }

    // Evento de click
    override fun onClick(v: View) {
        if (v.id == R.id.button_save)
            handleSave()
    }

    // Observadores da view model
    private fun observe() {
        viewModel.user.observe(this) {
            if (it.status())
                startActivity(Intent(this, MainActivity::class.java))
            else
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
        }
    }

    // Responsável por criar contas de usuarios
    private fun handleSave() {
        val name = binding.editName.text.toString()
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()
        viewModel.create(name, email, password)
    }
}