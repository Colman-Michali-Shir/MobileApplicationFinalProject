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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.foodie_finder.auth.AuthManager
import com.example.foodie_finder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding?.root)
//        UserModel.shared.loadUser()


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

        binding?.mainBottomNav?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController?.navigate(
                        R.id.homeFragment, null, NavOptions.Builder()
                            .setPopUpTo(R.id.homeFragment, true)
                            .build()
                    )
                    true
                }

                R.id.profileFragment -> {
                    navController?.navigate(
                        R.id.profileFragment, null, NavOptions.Builder()
                            .setPopUpTo(R.id.profileFragment, true)
                            .build()
                    )
                    true
                }

                R.id.savedPostsFragment -> {
                    navController?.navigate(
                        R.id.savedPostsFragment, null, NavOptions.Builder()
                            .setPopUpTo(R.id.savedPostsFragment, true)
                            .build()
                    )
                    true
                }

                R.id.searchFragment -> {
                    navController?.navigate(R.id.searchFragment)
                    true
                }

                R.id.newPostFragment -> {
                    navController?.navigate(R.id.newPostFragment)
                    true
                }

                else -> false
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

        val connectedUserId = AuthManager.shared.getCurrentUserUid()

        if (connectedUserId != null) {
            AuthManager.shared.connectUser(connectedUserId)
        }

        if (!AuthManager.shared.isUserLoggedIn()) {
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