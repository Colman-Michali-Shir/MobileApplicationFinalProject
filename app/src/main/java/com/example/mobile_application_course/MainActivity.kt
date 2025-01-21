package com.example.mobile_application_course


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolBar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolBar)

        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.main_nav_host) as? NavHostFragment
        navController = navHostFragment?.navController
        navController?.let {
            NavigationUI.setupActionBarWithNavController(
                activity = this,
                navController = it,
            )
            it.addOnDestinationChangedListener { _, _, _ ->
                invalidateOptionsMenu() // Refresh the menu when fragment changes
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("shir", "here")
        // the up/home button commonly represented by the back arrow in the ActionBar or Toolbar
        when (item.itemId) {
            android.R.id.home -> navController?.popBackStack()
            else -> navController?.let { NavigationUI.onNavDestinationSelected(item, it) }
        }
        return true
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val addMenuItem = menu?.findItem(R.id.newStudentFragment)
        val editMenuItem = menu?.findItem(R.id.editStudentFragment)

        navController?.currentDestination?.id.let { destinationId ->
            addMenuItem?.isVisible =
                destinationId != R.id.studentDetailsFragment // Show "Add" if not in StudentDetailsFragment
            editMenuItem?.isVisible =
                destinationId == R.id.studentDetailsFragment // Show "Edit" only in StudentDetailsFragment
        }

        return super.onPrepareOptionsMenu(menu)
    }
}