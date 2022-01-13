package com.example.exercisetracking.UI.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.exercisetracking.R
import com.example.exercisetracking.UI.AppViewModel
import com.example.exercisetracking.UI.StatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_stat.*
import kotlin.math.round

@AndroidEntryPoint // inject request data through Dagger Hilt into Fragment
class StatFragment: Fragment(R.layout.fragment_stat ) {

    private val viewModel : StatViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeObserver()
    }

    private fun observeObserver() {
        viewModel.totalDistance.observe(viewLifecycleOwner, Observer {
            it?.let{
                val km = it/1000f
                val totalDis = round(km * 10f)/10f
                val totalDistanStr = "${totalDis}km"
                totalDistance.text = totalDistanStr
            }
        })

        viewModel.totalCalorBurned.observe(viewLifecycleOwner, Observer {
            it?.let{
                val totalCalor = "${it}kcal"
                totalCalories.text = totalCalor
            }
        })
    }
}
