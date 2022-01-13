package com.example.exercisetracking.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.exercisetracking.Contants.RUNNING_TRACKING_FRAGMENT
import com.example.exercisetracking.Contants.WALKING_TRACKING_FRAGMENT
import com.example.exercisetracking.R
import com.example.exercisetracking.database.DataRecordDAO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint // This activity contains all fragments that exchange data with Database
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateToTracking(intent)

        setSupportActionBar(toolbar)
        bottomNavView.setupWithNavController(navControlCenter.findNavController())

        navControlCenter.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.walkingFragment, R.id.runningFragment, R.id.statFragment ->
                    bottomNavView.visibility = View.VISIBLE
                else -> bottomNavView.visibility = View.GONE


            }
        }
    }
    private fun navigateToTracking(intent: Intent?){
        if(intent?.action == RUNNING_TRACKING_FRAGMENT){
            navControlCenter.findNavController().navigate(R.id.action_to_Running_Tacking)
        }
        else if(intent?.action == WALKING_TRACKING_FRAGMENT){
            navControlCenter.findNavController().navigate(R.id.action_to_Waling_Tacking)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTracking(intent)
    }

}