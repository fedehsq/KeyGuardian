package com.example.keyguardian.activities

import EncryptedSharedPreferencesUtils
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keyguardian.R
import com.example.keyguardian.adapters.SecretsAdapter
import com.example.keyguardian.databinding.ActivitySecretsListBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson


class SecretsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecretsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivitySecretsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val prefs = EncryptedSharedPreferencesUtils.getInstance(this)
        // Create a json secret object
        val mapSecret = HashMap<String, String>()
        mapSecret["username"] = "test"
        mapSecret["password"] = "test"
        prefs.putString("credentials", Gson().toJson(mapSecret))
        prefs.putString("test2", "test2")
        val secretsName = prefs.getKeys()

        binding.secretsList.adapter = SecretsAdapter(secretsName.toList())
        binding.secretsList.layoutManager = LinearLayoutManager(this)
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }
    }
}