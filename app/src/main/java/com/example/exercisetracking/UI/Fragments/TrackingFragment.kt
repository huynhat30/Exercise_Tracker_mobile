package com.example.exercisetracking.UI.Fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.exercisetracking.R
import com.example.exercisetracking.UI.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // inject request data from Database through Dagger Hilt into Fragment
class TrackingFragment: Fragment(R.layout.fragment_tracking_walk ) {
    private val viewModel : AppViewModel by viewModels()
}