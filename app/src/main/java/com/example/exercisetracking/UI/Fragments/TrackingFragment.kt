package com.example.exercisetracking.UI.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.exercisetracking.Contants
import com.example.exercisetracking.R
import com.example.exercisetracking.UI.AppViewModel
import com.example.exercisetracking.service.TrackerService
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking_run.*

@AndroidEntryPoint // inject request data from Database through Dagger Hilt into Fragment
class TrackingFragment: Fragment(R.layout.fragment_tracking_walk ) {
    private val viewModel : AppViewModel by viewModels()

    private var map: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        backBtn.setOnClickListener{
            findNavController().navigate(R.id.action_trackingFragment_to_walkingFragment)
        }
        mapView.getMapAsync{
            map = it
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    private fun commandToService(action:String) = Intent(requireContext(), TrackerService::class.java).also{
        it.action = action
        requireContext().startService(it)
    }
}
