package com.example.exercisetracking.UI.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.exercisetracking.R
import kotlinx.android.synthetic.main.fragment_startup.*

/**
 * Created by Nhat Huy Giang
 * Stydent ID: 20313345
 * Email: psyng1@nottingham.ac.uk
 */
class StartUpFragment: Fragment(R.layout.fragment_startup ) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ContinueButton.setOnClickListener{
            findNavController().navigate(R.id.action_startUpFragment_to_walkingFragment)
        }
    }
}