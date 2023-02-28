package com.example.keyguardian.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keyguardian.R
import com.example.keyguardian.adapters.SECRET_EXTRA
import com.example.keyguardian.adapters.SecretContentAdapter
import com.example.keyguardian.databinding.ActivityEditSecretBinding
import com.example.keyguardian.models.Secret

class EditSecretActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSecretBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_secret)
        binding = ActivityEditSecretBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // Read the secret from the intent
        val jsonSecret = intent.getStringExtra(SECRET_EXTRA)
        val secret = Secret.fromJson(jsonSecret!!)

        binding.secretContentList.adapter = SecretContentAdapter(secret)
        binding.secretContentList.layoutManager = LinearLayoutManager(this)


    }
}