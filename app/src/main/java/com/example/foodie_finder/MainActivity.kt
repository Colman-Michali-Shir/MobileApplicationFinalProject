package com.example.foodie_finder

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.foodie_finder.databinding.ActivityMainBinding
import com.example.foodie_finder.ui.view.HomeFragmentDirections
import com.example.foodie_finder.ui.view.PostDetailsFragmentDirections
import com.example.foodie_finder.ui.viewModel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private var binding: ActivityMainBinding? = null
    private var viewModel: MainActivityViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding?.root)
        viewModel?.initUser()

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
                destination.id != R.id.homeFragment
                        && destination.id != R.id.loginFragment
                        && destination.id != R.id.registerFragment
            )
        }

        binding?.mainBottomNav?.visibility = View.VISIBLE

        if (viewModel?.isUserLoggedIn() == false) {
            Log.d("TAG", "userIsnt logged in")
            binding?.mainBottomNav?.visibility = View.GONE
            navController?.navigate(R.id.loginFragment)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val currentDestination = navController?.currentDestination

        when (currentDestination?.id) {
            R.id.homeFragment -> {
                menuInflater.inflate(R.menu.logout_menu, menu)
            }

            R.id.postDetailsFragment -> {
                menuInflater.inflate(R.menu.menu_edit_student, menu)
            }

            else -> {
                menu?.clear()
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController?.popBackStack()
                true
            }

            R.id.newPostFragment -> {
                val action =
                    PostDetailsFragmentDirections.actionGlobalNewPostFragment()
                navController?.navigate(action)
                true
            }

            R.id.logout -> {
                viewModel?.signOut()
                val action =
                    HomeFragmentDirections.actionStudentsListFragmentToLoginFragment()
                navController?.navigate(action)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}