package com.devmasterteam.tasks.service.repository

import com.google.gson.Gson

open class BaseRepository {


    fun failResponse(str: String): String {
        return Gson().fromJson(str, String::class.java)
    }
}