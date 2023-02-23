package com.example.keyguardian

import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.security.crypto.MasterKeys
import com.example.keyguardian.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import javax.crypto.KeyGenerator


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    private val AUTHENTICATION_DURATION_SECONDS = 30


    private val TAG = "MyActivity"

    @RequiresApi(Build.VERSION_CODES.P)
    fun authenticate() {
        return BiometricManager.from(this).canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        Log.v(TAG, "Before auth")
        // Attempt to authenticate the user
        /*val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (keyguardManager.isKeyguardSecure) {
            BiometricPrompt.Builder(this)
                .setTitle("Authenticate to access your passwords")
                .setSubtitle("Enter your PIN or pattern to access your passwords")
                .setNegativeButton("Cancel", this.mainExecutor, { dialog, which ->
                    // Handle negative button click
                })
                .build()
                .authenticate(
                    BiometricPrompt.CryptoObject(Cipher.getInstance("AES/GCM/NoPadding"))
                )
            val intent = keyguardManager.createConfirmDeviceCredentialIntent(
                "Authenticate to access your passwords",
                "Enter your PIN or pattern to access your passwords"
            )
            if (intent != null) {
                startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS)
            }
        }*/
        try {

            val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
            val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

            // Set up the key generator parameter specifications
            val builder = KeyGenParameterSpec.Builder(
                mainKeyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            builder
                .setUserAuthenticationRequired(true)
                .setUserAuthenticationParameters(
                    AUTHENTICATION_DURATION_SECONDS,
                    KeyProperties.AUTH_DEVICE_CREDENTIAL
                )

            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
            )
            keyGenerator.init(builder.build())
            val key = keyGenerator.generateKey()
            // print key
            Log.v(TAG, key.toString())


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

}