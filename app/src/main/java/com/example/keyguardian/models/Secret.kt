package com.example.keyguardian.models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Secret(var name: String, var secretContent: MutableList<KeyValuePair>) {
    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Secret(name='$name', secretContent=$secretContent)"
    }

    companion object {
        fun fromJson(json: String): Secret {
            return Gson().fromJson(json, object : TypeToken<Secret>() {}.type)
        }
    }
}