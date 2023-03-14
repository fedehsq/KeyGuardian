package com.example.keyguardian.security

import android.app.KeyguardManager
import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.security.crypto.MasterKey

class MasterKeyUtils {

    companion object {
        private const val TAG = "MasterKeyUtils"
        private const val authDurationSeconds = 30

        /**
         * Set up the master key with user authentication required
         */
        fun getMasterKey(context: Context): MasterKey {
            // Set up the key generator parameter specifications
            try {
                val keyGen = KeyGenParameterSpec.Builder(
                    MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                keyGen
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setUserAuthenticationRequired(true)
                    .setUserAuthenticationParameters(
                        authDurationSeconds,
                        KeyProperties.AUTH_DEVICE_CREDENTIAL or KeyProperties.AUTH_BIOMETRIC_STRONG
                    )
                    .setKeySize(256)

                return MasterKey.Builder(context)
                    .setKeyGenParameterSpec(keyGen.build())
                    .build()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to setup master key: ${e.message}")
                throw Exception("Failed to setup master key: ${e.message}")
            }
        }
    }
}