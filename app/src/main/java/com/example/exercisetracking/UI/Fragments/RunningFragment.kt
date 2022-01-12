package com.example.exercisetracking.UI.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.exercisetracking.R
import com.example.exercisetracking.UI.AppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_running.*

@AndroidEntryPoint // inject request data through Dagger Hilt into Fragment
class RunningFragment: Fragment(R.layout.fragment_running ) {

    private val viewModel : AppViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener{
            findNavController().navigate(R.id.action_runningFragment_to_trackingFragment)
        }
    }
}