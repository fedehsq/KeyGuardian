package com.example.keyguardian.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.keyguardian.R
import com.example.keyguardian.databinding.ActivityMainBinding
import com.example.keyguardian.security.SecurityUtils
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MyActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /*
    private fun auth() {

        val biometricManager =
            applicationContext.getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e(
                    TAG, "The user hasn't associated " +
                            "any biometric credentials with their account."
                )
            }
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.v(TAG, "App can authenticate using biometrics.")
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e(TAG, "No biometric features available on this device.")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e(TAG, "Biometric features are currently unavailable.")
            }

            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                Log.e(TAG, "The security update is required.")
            }
        }

        val authenticator = BiometricPrompt.Builder(this)
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .setTitle("Authenticate to access your passwords")
            .setSubtitle("Enter your PIN or pattern to access your passwords")
            .build()

        // Handle the authentication result
        authenticator.authenticate(
            CancellationSignal(),
            ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    // The user has been successfully authenticated
                    Log.v(TAG, "Auth success")
                    // get the master key

                    try {
                        val sharedPreferences = EncryptedSharedPreferences.create(
                            applicationContext,
                            "secret_settings",
                            masterKey,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        )

                        with(sharedPreferences.edit()) {
                            // Edit the user's shared preferences...
                            // Write the user's name to the preferences
                            putString("name", "John")
                            apply()
                        }

                        // Get the user's name from the preferences
                        val name = sharedPreferences.getString("name", null)
                        Log.v(TAG, "Name: $name")

                    } catch (e: Exception) {
                        Log.e(TAG, "Error: $e")
                    }

                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    // An error occurred during authentication
                }

                override fun onAuthenticationFailed() {
                    // Authentication failed
                }
            }
        )
    }

     */


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        render()
    }

    private fun render() {
        if (!SecurityUtils.setupSecurity(this)) {
            Log.e(TAG, "Failed to setup security")
            binding.authenticateButton.setOnClickListener {
                // Launch this activity again to setup security
                render()
            }
        } else {
            Log.v(TAG, "Security setup successful")
            binding.text.text = "Security setup successful"
            binding.authenticateButton.visibility = View.GONE
            binding.fab.visibility = View.VISIBLE
            binding.fab.setOnClickListener { view ->
                binding.text.text = "Hi, World!"
                Snackbar.make(view, binding.text.text, Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab)
                    .setAction("Action", null).show()
            }
        }
    }


}