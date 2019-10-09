package com.aviationdata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val navigationController: NavController by lazy {
        findNavController(R.id.navigation_host_fragment)
    }

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(navigationController.graph)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)

        setupActionBarWithNavController(navigationController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(navigationController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}
