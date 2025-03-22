package com.example.foodie_finder


import android.os.Bundle
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
import com.example.foodie_finder.databinding.ActivityMainBinding
import com.example.foodie_finder.model.Model

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding?.root)
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
        }

        if (!Model.shared.isUserLoggedIn()) {
            navController?.navigate(R.id.loginFragment)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val currentDestination = navController?.currentDestination

        when (currentDestination?.id) {
            R.id.studentsListFragment -> {
                menu?.clear()
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                menuInflater.inflate(R.menu.menu, menu)
            }

            R.id.studentDetailsFragment -> {
                menuInflater.inflate(R.menu.menu_edit_student, menu)
            }

            R.id.loginFragment -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
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

            R.id.newStudentFragment -> {
                val action =
                    StudentsListFragmentDirections.actionGlobalNewStudentFragment()
                navController?.navigate(action)
                true
            }

            R.id.logout -> {
                Model.shared.signOut()
                val action =
                    StudentsListFragmentDirections.actionStudentsListFragmentToLoginFragment()
                navController?.navigate(action)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}