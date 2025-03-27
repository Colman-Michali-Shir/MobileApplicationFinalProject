package com.example.foodie_finder

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.foodie_finder.auth.AuthManager
import com.example.foodie_finder.data.model.UserModel
import com.example.foodie_finder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding?.root)
        UserModel.shared.loadUser()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolBar: Toolbar = findViewById(R.id.main_toolbar)
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        setSupportActionBar(toolBar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarTitle.text = getString(R.string.app_name)

        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.main_nav_host) as? NavHostFragment

        navController = navHostFragment?.navController

        navController?.let {
            NavigationUI.setupActionBarWithNavController(
                activity = this,
                navController = it,
            )
        }

        binding?.mainBottomNav?.let { mainBottomNav ->
            navController?.let { navController ->
                NavigationUI.setupWithNavController(
                    mainBottomNav,
                    navController
                )
            }
        }

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.setDisplayHomeAsUpEnabled(
                false
            )
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    binding?.mainBottomNav?.visibility = View.GONE
                    invalidateOptionsMenu()
                }

                else -> {
                    binding?.mainBottomNav?.visibility = View.VISIBLE
                    invalidateOptionsMenu()
                }
            }
        }

        if (!UserModel.shared.isUserLoggedIn()) {
            navController?.navigate(R.id.loginFragment)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        val currentDestination = navController?.currentDestination
        if (currentDestination?.id != R.id.loginFragment && currentDestination?.id != R.id.registerFragment) {
            menuInflater.inflate(
                R.menu.logout_menu,
                menu
            )
            val logoutItem = menu?.findItem(R.id.logout)
            logoutItem?.icon?.setTint(getColor(R.color.white))
        }


        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.logout -> {
                AuthManager.shared.signOut()
                navController?.navigate(R.id.action_global_loginFragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}