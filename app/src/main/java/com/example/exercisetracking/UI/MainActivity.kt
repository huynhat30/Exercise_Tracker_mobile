package com.example.exercisetracking.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
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
}