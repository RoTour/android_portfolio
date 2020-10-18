package com.novyapp.test.utlimatetodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.novyapp.test.utlimatetodolist.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

		val navController = findNavController(R.id.navHostFragment)
		drawerLayout = binding.drawerLayout

		NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
		NavigationUI.setupWithNavController(binding.navView, navController)
    }

	override fun onSupportNavigateUp(): Boolean {
		val navController = findNavController(R.id.navHostFragment)
		return NavigationUI.navigateUp(navController, drawerLayout)
	}
}