package com.example.keyguardian.activities

import LockscreenUtils
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!LockscreenUtils.hasLockScreen(this)) {
            // Launch authenticate activity
            val intent = Intent(this, LockscreenActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, AuthenticateActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}