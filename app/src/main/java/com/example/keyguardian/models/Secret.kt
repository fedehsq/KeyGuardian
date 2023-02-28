package com.example.keyguardian.models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Secret(var content: List<KeyValuePair>) {

    fun getContent(): List<KeyValuePair> {
        return content
    }

    companion object {
        fun fromJson(json: String): Secret {
            val type = object : TypeToken<Secret>() {}.type
            return Gson().fromJson(json, type)
        }
    }
}