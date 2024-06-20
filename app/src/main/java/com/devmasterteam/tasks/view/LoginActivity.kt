package com.devmasterteam.tasks.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Layout
        setContentView(binding.root)

        // Eventos
        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)

        // Verifica se o usuário ja foi logado anteriormente
        viewModel.verifyLoggedUser()

        // Observadores
        observe()

        // Esconder a barra da tela
        supportActionBar?.hide()

    }

    // Eventos de click dos butões
    override fun onClick(v: View) {
        if (v.id == R.id.button_login)
            handleLogin()
        else if (v.id == R.id.text_register)
            startActivity(Intent(this, RegisterActivity::class.java))
    }

    // Observadores da view model
    private fun observe() {
        viewModel.login.observe(this) {
            if (it.status()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loggedUser.collect { value ->
                    if (value) {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    // Responsável por fazer o login na conta
    private fun handleLogin() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()
        viewModel.doLogin(email, password)
    }
}