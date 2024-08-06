package com.synrgy7team4.bankingapps

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_feature_auth) as NavHostFragment
        val navController = navHostFragment.navController

        // Navigate to the main destination

        val sharedPreferences: SharedPreferences = getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token != null) {
            navController.navigate(com.synrgy7team4.feature_auth.R.id.homeFragment)
        } else {
            navController.navigate(com.synrgy7team4.feature_auth.R.id.splashScreenFragment)

        }
//        navController.navigate(com.synrgy7team4.feature_auth.R.id.splashScreenFragment)


    }
}