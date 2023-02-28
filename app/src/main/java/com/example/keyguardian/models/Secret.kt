package com.example.keyguardian.models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Secret(var secretContent: List<KeyValuePair>) {
    fun toJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromJson(json: String): Secret {
            return Gson().fromJson(json, object : TypeToken<Secret>() {}.type)
        }
    }
}