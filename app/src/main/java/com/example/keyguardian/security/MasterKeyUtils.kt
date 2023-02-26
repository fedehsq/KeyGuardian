package com.example.keyguardian.security
import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.MasterKey

class MasterKeyUtils {

    companion object {
        private const val TAG = "MasterKeyUtils"
        private const val authDurationSeconds = 30
        private lateinit var masterKey: MasterKey

        /**
         * Set up the master key with user authentication required
         */
        fun setupMasterKey(context: Context) {
            // Set up the key generator parameter specifications
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
                    KeyProperties.AUTH_DEVICE_CREDENTIAL
                )
                .setKeySize(256)
            this.masterKey = MasterKey.Builder(context)
                .setKeyGenParameterSpec(keyGen.build())
                .build()
        }

        /**
         * Get the master key
         */
        fun getMasterKey(): MasterKey {
            if (!this::masterKey.isInitialized) {
                throw Exception("Master key not initialized")
            }
            // get the master key with library function
            return this.masterKey
        }


    }
}