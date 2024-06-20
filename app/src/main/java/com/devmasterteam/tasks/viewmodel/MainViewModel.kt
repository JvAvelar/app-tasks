package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Instancia do SecurityPreferences
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    // Variavel a ser observada
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    // Responsável por fazer o logout do usuário
    fun logout(){
        securityPreferences.remove(TaskConstants.SHARED.TOKEN_KEY)
        securityPreferences.remove(TaskConstants.SHARED.PERSON_KEY)
        securityPreferences.remove(TaskConstants.SHARED.PERSON_NAME)
    }

    // Responsável por buscar o nome salvo do usuário
    fun loadUserName() {
        _name.value = securityPreferences.get(TaskConstants.SHARED.PERSON_NAME)
    }
}