package com.synrgy7team4.feature_dashboard.presentation


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.synrgy7team4.feature_dashboard.databinding.ActivityDashboardBinding


class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostDashboard = supportFragmentManager.findFragmentById(com.synrgy7team4.feature_dashboard.R.id.nav_host_fragment_feature_dashboard) as NavHostFragment
        val navController = navHostDashboard.navController


        val navView: BottomNavigationView = binding.navView
        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                com.synrgy7team4.feature_dashboard.R.id.navigation_home, com.synrgy7team4.feature_dashboard.R.id.navigation_dashboard, com.synrgy7team4.feature_dashboard.R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)*/
        navView.setupWithNavController(navController)

    }
}