package com.example.todo.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.about.AboutActivity
import com.example.todo.R
import com.example.todo.R.id
import com.example.todo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportFragmentManager.findFragmentById(id.nav_host)?.findNavController()
            ?.let { nav ->
                appBarConfiguration = AppBarConfiguration(nav.graph)
                setupActionBarWithNavController(nav, appBarConfiguration)
            }
    }

    override fun onSupportNavigateUp() =
        navigateUp(findNavController(id.nav_host), appBarConfiguration)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            id.about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            id.settings -> {
                findNavController(id.nav_host).navigate(id.editPrefs)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}