package com.example.keyguardian

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.keyguardian.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    private val REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 1
    private val KEY_NAME = "example_key"
    private val PREFERENCE_FILE_KEY = "example_pin"
    private val AUTHENTICATION_DURATION_SECONDS = 30

    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator


    private val TAG = "MyActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        Log.v(TAG, "Before auth");
        // Attempt to authenticate the user
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (keyguardManager.isKeyguardSecure) {
            val intent = keyguardManager.createConfirmDeviceCredentialIntent(
                "Authenticate to access your passwords",
                "Enter your PIN or pattern to access your passwords"
            )
            if (intent != null) {
                startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS)
            }
        }

        keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

        // Set up the key generator parameter specifications
        val builder = KeyGenParameterSpec.Builder(
            KEY_NAME, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
        builder.setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(true)
            .setUserAuthenticationValidityDurationSeconds(AUTHENTICATION_DURATION_SECONDS)

        keyGenerator.init(builder.build())
        keyGenerator.generateKey()

        try {
            keyStore.load(null)

        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }


        /* Set up the cipher
        cipher = Cipher.getInstance(
            KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_GCM + "/"
                    + KeyProperties.ENCRYPTION_PADDING_NONE
        )*/


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            binding.hiText.text = "Hi, World!"
            Snackbar.make(view, binding.hiText.text, Snackbar.LENGTH_LONG).setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            if (resultCode == Activity.RESULT_OK) {
                Log.v(TAG, "Authenticated")
            } else {
                // User has cancelled or failed to authenticate, handle this as appropriate
                // ...
                Log.v(TAG, "Not Authenticated")
            }
        }
    }
}