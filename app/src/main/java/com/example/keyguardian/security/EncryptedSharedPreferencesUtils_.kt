package com.example.keyguardian.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncryptedSharedPreferencesUtils_ {

    // Singleton pattern
    companion object {
        private var sharedPreferences: SharedPreferences? = null
        private lateinit var applicationContext: Context
        private lateinit var masterKey: MasterKey

        fun init(applicationContext: Context, masterKey: MasterKey) {
            this.applicationContext = applicationContext
            this.masterKey = masterKey
            sharedPreferences = EncryptedSharedPreferences.create(
                applicationContext,
                "secret_settings",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        }
        fun getInstance() {
            if (sharedPreferences == null) {
                sharedPreferences = EncryptedSharedPreferences.create(
                    applicationContext,
                    "secret_settings",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
            }
        }

        // write to shared preferences
        fun writeStringToSharedPreferences(key: String, value: String) {
            // TODO
        }
    }

}