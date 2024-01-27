package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.google.gson.Gson
import retrofit2.Response
import java.util.Objects

open class BaseRepository {


    private fun failResponse(str: String): String {
        return Gson().fromJson(str, String::class.java)
    }

    fun <T> handleResponse(response: Response<T>, listener: APIListener<T>) {
        if (response.code() == TaskConstants.HTTP.SUCCESS)
            response.body()?.let { listener.onSuccess(it) }
        else
            listener.onFailure(failResponse(response.errorBody()!!.string()))
    }

    fun <T> failureError(listener: APIListener<T>, context: Context) {
        listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
    }
}