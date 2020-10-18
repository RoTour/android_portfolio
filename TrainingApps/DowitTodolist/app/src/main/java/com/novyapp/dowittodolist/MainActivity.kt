package com.novyapp.dowittodolist

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.navigation.ActivityNavigator
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.novyapp.dowittodolist.createtodo.CreateTodoFragment
import com.novyapp.dowittodolist.main.MainFragment
import com.novyapp.dowittodolist.main.MainFragmentDirections
import com.novyapp.dowittodolist.utils.mylogs
import kotlinx.android.synthetic.main.main_fragment.*


lateinit var toolbar: Toolbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        val navController = this.findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)


        toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }


        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.addTodo -> {
                    // Handle search icon press
                    Log.i("MainActivity", "Launch new Todo Fragment")
                    navController.navigate(MainFragmentDirections.actionMainFragmentToCreateTodoFragment())
                    true
                }
                R.id.about -> {
                    // Handle more item (inside overflow menu) press
                    Log.i("MainActivity", "Launch web page")
                    true
                }

                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}
