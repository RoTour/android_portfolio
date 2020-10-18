package com.novyapp.test.fasttypingtraining

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.novyapp.test.fasttypingtraining.databinding.ActivityMainBinding

private const val PACKAGE_NAME = "com.novyapp.ftta"
const val REPOSITORY_SHARED_PREFS = "$PACKAGE_NAME.repoprefs"
var toolbar: Toolbar? = null

/**
 * Getting the repository in the main activity reduces the waiting time when wanting to start a
 * game and a database update is needed
 */


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        toolbar = binding.mainToolbar
        setSupportActionBar(toolbar)

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