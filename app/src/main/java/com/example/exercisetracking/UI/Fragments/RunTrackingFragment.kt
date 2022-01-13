package com.example.exercisetracking.UI.Fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.exercisetracking.Contants.PAUSE_SERVICE_ACTION
import com.example.exercisetracking.Contants.START_RESUME_SERVICE_ACTION
import com.example.exercisetracking.Contants.STOP_SERVICE_ACTION
import com.example.exercisetracking.R
import com.example.exercisetracking.UI.AppViewModel
import com.example.exercisetracking.service.Polyline
import com.example.exercisetracking.service.TimerConverter
import com.example.exercisetracking.service.TrackerService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking_run.*

@AndroidEntryPoint // inject request data from Database through Dagger Hilt into Fragment
class RunTrackingFragment: Fragment(R.layout.fragment_tracking_run ) {
    private val viewModel : AppViewModel by viewModels()

    private var isTracking = false // Boolean to tacking
    private var pathPoints = mutableListOf<Polyline>() // List of point coordinate
    private var map: GoogleMap? = null // use Google Map as default tracking map
    private var currentTimeMilli = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        //Button: start tracking service
        startRunBtn.setOnClickListener{
            toggleRun()
        }

        //Button: return to list of run
        backBtn.setOnClickListener{
            findNavController().navigate(R.id.action_runTrackingFragment_to_runningFragment)
        }

        //Sync map
        mapView.getMapAsync{
            map = it
            drawPonyLine()
        }
        observeObserver()
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
        mapView.onSaveInstanceState(outState) // check for the life circle state of application
    }

    //Method: send command to service
    private fun commandToService(action:String) = Intent(requireContext(), TrackerService::class.java).also{
        it.action = action // action on START/RESUME/STOP/PAUSE service
        requireContext().startService(it)
    }

    // Method: connect two point coordinate to draw pony line
    private fun connectPointPnyLine(){
        // Check if pony line not empty and there more than 1 point coordinate
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1){
            val prePrePoint = pathPoints.last()[pathPoints.last().size - 2] // The pre of pre point
            val prePoint = pathPoints.last().last() // The pre point
            val polyLineStyle = PolylineOptions().color(Color.BLUE) //Set color for pony line
                                .width(10f) // Set width for pony line
                                .add(prePrePoint) //Add the pre of pre point to line
                                .add(prePoint) //Add the pre point to line
            map?.addPolyline(polyLineStyle) // add poly line to tracking map
        }
    }

    // Method: connect all point and draw complete pony line
    private fun drawPonyLine(){
        for(ponyLine in pathPoints){
            val polyLineStyle = PolylineOptions().color(Color.BLUE) //Set color for pony line
                .width(10f) // Set width for pony line
                .addAll(ponyLine)
            map?.addPolyline(polyLineStyle)
        }
    }

    //Method: center camera to user
    private fun centCameraToUser(){
        // Check if pony line not empty and there more than 0 point coordinate
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 0){
            // move camera to the coordinate of the pre point and zoom
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(pathPoints.last().last(), 15f )
            )
        }
    }

    //Method: observe data from service and react to the changes
    // Method: update tracking
    private fun updateTracking(isTracking: Boolean){
        this.isTracking = isTracking
        if(!isTracking){
            startRunBtn.text = "Start"
            finishRunBtn.visibility = View.VISIBLE
        }
        else{
            startRunBtn.text = "Stop"
            finishRunBtn.visibility = View.GONE
        }
    }

    private fun toggleRun(){
        if(isTracking){
            commandToService(PAUSE_SERVICE_ACTION)
        }
        else{
            commandToService(START_RESUME_SERVICE_ACTION)
        }
    }

    //Method: observe data from service and react to the changes
    private fun observeObserver(){
        TrackerService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })
        TrackerService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            connectPointPnyLine()
            centCameraToUser()
        })

        TrackerService.timeInMilli.observe(viewLifecycleOwner, Observer {
            currentTimeMilli = it
            val timeFormatted = TimerConverter.getTimerFormatted(currentTimeMilli, true)
            timerRun.text = timeFormatted
        })
    }

}