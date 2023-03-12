package com.example.keyguardian.activities

import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.keyguardian.databinding.ActivityAuthenticateBinding

private const val TAG = "ActivityAuthenticate"
private const val MAX_AUTH_ATTEMPTS = 3

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticateBinding
    private var authAttempts = 0

    private fun authenticate() {
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
                    val intent = Intent(this@AuthenticationActivity, SecretsListActivity::class.java)
                    startActivity(intent)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    // An error occurred during authentication
                    Log.e(TAG, "Auth error: $errString")
                    authAttempts++
                    Toast.makeText(
                        this@AuthenticationActivity,
                        "Authentication error: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationFailed() {
                    // Authentication failed
                    Log.e(TAG, "Auth failed")
                    authAttempts++
                    Toast.makeText(
                        this@AuthenticationActivity,
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        authenticate()
        binding.authenticateButton.setOnClickListener {
            if (authAttempts < MAX_AUTH_ATTEMPTS) {
                authenticate()
            } else {
                Toast.makeText(
                    this,
                    "Too many authentication attempts",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}