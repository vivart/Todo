package com.example.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.about.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = getString(R.string.app_name)
        binding.webView.loadUrl("file:///android_asset/about.html")
    }
}
