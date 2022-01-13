package com.example.exercisetracking.UI.Fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.exercisetracking.Contants.PAUSE_SERVICE_ACTION
import com.example.exercisetracking.Contants.START_RESUME_SERVICE_ACTION
import com.example.exercisetracking.Contants.STOP_SERVICE_ACTION
import com.example.exercisetracking.R
import com.example.exercisetracking.UI.AppViewModel
import com.example.exercisetracking.UI.tracking.permissionTracking
import com.example.exercisetracking.database.PhysActivity
import com.example.exercisetracking.service.Polyline
import com.example.exercisetracking.service.TimerConverter
import com.example.exercisetracking.service.TrackerService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking_run.*
import java.util.*
import kotlin.math.round

/**
 * Created by Nhat Huy Giang
 * Stydent ID: 20313345
 * Email: psyng1@nottingham.ac.uk
 */
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

        //Button: cancel a run
        cancelBtn.setOnClickListener{
            showCancelDialog()
        }

        //Button: finish a run and save to DB
        finishRunBtn.setOnClickListener{
            //zoomWholeTrack()
            endSaveRubToDB()
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
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
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
            cancelBtn.visibility = View.VISIBLE
        }
        else{
            startRunBtn.text = "Stop"
            finishRunBtn.visibility = View.GONE
            cancelBtn.visibility = View.GONE
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

    //Method: create an alert dialog
    private fun showCancelDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_Material3_Dialog_Alert)
                    .setTitle("Cancel the exercise?")
                    .setMessage("Are you sure to cancel the run and delete all its data?")
                    .setIcon(R.drawable.ic_delete)
                    .setPositiveButton("Yes"){ _, _ -> stopRun()}
                    .setNegativeButton("No"){dialog, which -> showCancelDialog()}
                    .create()
            dialog.show()
    }

    // Method: send stop command to service
    private fun stopRun() {
        commandToService(STOP_SERVICE_ACTION)
        findNavController().navigate(R.id.action_runTrackingFragment_to_runningFragment)
    }

    //Method: after finish a run, capture screen of track on map
    private fun zoomWholeTrack(){
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoints){
            for (pos in polyline){
                bounds.include(pos)
            }
        }
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                mapView.width,
                mapView.height,
                (mapView.height * 0.05f).toInt()
            )
        )
    }

    //Method: save all information to DB
    private fun endSaveRubToDB() {
        //Screenshot the current track
        map?.snapshot { bmp ->
            var disInMeters = 0
            for(polyline in pathPoints) {
                disInMeters += permissionTracking.calculatePolylineLength(polyline).toInt()
            }

            //DB property: type
            val type = "Run"
            //DB property: avgVelKMH
            val avgSpeed = round((disInMeters / 1000f) / (currentTimeMilli / 1000f / 60 / 60) * 10) / 10f
            //DB property: dateRecord
            val dateTimestamp = Calendar.getInstance().timeInMillis
            //DB property: calorBurned
            val caloriesBurned = ((disInMeters / 1000f) * 60).toInt()
            //DB property summary
            val run = PhysActivity(type, bmp, dateTimestamp,currentTimeMilli, avgSpeed, 0, disInMeters, caloriesBurned)
            viewModel.insertRun(run)
            Snackbar.make(
                requireActivity().findViewById(R.id.rootView),
                "Run saved successfully",
                Snackbar.LENGTH_LONG
            ).show()
            stopRun()
        }
    }

}