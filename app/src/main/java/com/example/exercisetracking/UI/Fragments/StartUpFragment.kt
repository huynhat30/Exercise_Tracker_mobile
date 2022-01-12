package com.example.exercisetracking.UI.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.exercisetracking.R
import kotlinx.android.synthetic.main.fragment_startup.*

class StartUpFragment: Fragment(R.layout.fragment_startup ) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ContinueButton.setOnClickListener{
            findNavController().navigate(R.id.action_startUpFragment_to_walkingFragment)
        }
    }
}