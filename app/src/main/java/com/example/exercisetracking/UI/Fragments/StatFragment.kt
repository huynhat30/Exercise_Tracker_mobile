package com.example.exercisetracking.UI.Fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.exercisetracking.R
import com.example.exercisetracking.UI.AppViewModel
import com.example.exercisetracking.UI.StatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // inject request data through Dagger Hilt into Fragment
class StatFragment: Fragment(R.layout.fragment_stat ) {

    private val viewModel : StatViewModel by viewModels()
}