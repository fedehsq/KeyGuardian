package com.example.keyguardian.activities

import LockscreenUtils
import android.app.PendingIntent
import android.app.admin.DevicePolicyManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowCompat
import com.example.keyguardian.databinding.ActivityLockscreenBinding

private const val TAG = "ActivityAuthenticate"

class LockscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLockscreenBinding

    private val requestPinSetup = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        Log.v(TAG, "Pin setup result: $result")

        if (!LockscreenUtils.hasLockScreen(this)) {
            Log.e(TAG, "Lock screen not set up")
            Toast.makeText(
                this,
                "You must set up a lock screen to use this app",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Log.v(TAG, "Lock screen set up")
            // Launch MainActivity
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLockScreen() {
        val intent = Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD)
        if (intent.resolveActivity(packageManager) != null) {
            val intentSender = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            ).intentSender
            val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()
            requestPinSetup.launch(intentSenderRequest)
        } else {
            Toast.makeText(this, "No activity found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityLockscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.lockscreenButton.setOnClickListener {
            setupLockScreen()
        }
    }
}