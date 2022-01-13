package com.example.exercisetracking.service


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.exercisetracking.Contants.PAUSE_SERVICE_ACTION
import com.example.exercisetracking.Contants.RUNNING_TRACKING_FRAGMENT
import com.example.exercisetracking.Contants.START_RESUME_SERVICE_ACTION
import com.example.exercisetracking.Contants.STOP_SERVICE_ACTION
import com.example.exercisetracking.Contants.WALKING_TRACKING_FRAGMENT
import com.example.exercisetracking.R
import com.example.exercisetracking.UI.MainActivity
import com.example.exercisetracking.UI.tracking.permissionTracking
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class TrackerService : LifecycleService(){

    private val CHANNEL_ID = "tracking_channel"
    private val CHANNEL_NAME = "Tracking"
    private val NOTIFICATION_ID = 1
    private val UPDATE_LOCATION_INTERVAL = 5000L // each 5 seconds update 1 time
    private val FASTEST_LOCATION_INTERVAL = 2000L // the lowest possible interval to update location
    private val timeInSeconds = MutableLiveData<Long>() // recorded time in second

    private var fisrtRun = true //Boolen if is that first run
    private var isTimerEnable = false // Boolen if timer is enabled
    private var lapTime = 0L //initial time
    private var totalTime = 0L // Total time of run
    private var timeToggle = 0L // Time when touch Start button
    private var lastSecondTimeStamp = 0L // Last whole second value passed in millisecond

    // request location update provide by Google
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // Object for background tracking
    companion object {
        val isTracking = MutableLiveData<Boolean>() // Boolean to tracking
        val pathPoints = MutableLiveData<Polylines>() // A group of list of coordinate to draw a line
        val timeInMilli = MutableLiveData<Long>() // recorded time in millisecond
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if(it.action == START_RESUME_SERVICE_ACTION){
                    if(fisrtRun){
                        startForegroundService()
                        fisrtRun = false
                    }
                else{
                    timer()
                    Log.d("service_action","Service resume")}
                }
            else if(it.action == PAUSE_SERVICE_ACTION){
                    pauseService()
                    Log.d("service_action","Service pause")
                }
            else if(it.action == STOP_SERVICE_ACTION) {
                Log.d("service_action", "Service stop")
            }
            else{
                Log.d("service_action", "No command found")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    // Method: create notification channel for foreground activity
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_LOW)

        notificationManager.createNotificationChannel(channel)
    }

    //Method: active foregroundService
    private fun startForegroundService(){
        timer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setAutoCancel(false) // Prevent the notification disappear when user touch it
            .setOngoing(true) // Notification can be swiped
            .setSmallIcon(R.drawable.ic_notifi_exercise) // Set notification icon
            .setContentTitle("Tracking Application")
            .setContentText("App is running")
            .setContentIntent(getMainActivityPendingIntent())

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this, 0,
        Intent(this, MainActivity::class.java).also {
            it.action = RUNNING_TRACKING_FRAGMENT},
        FLAG_UPDATE_CURRENT
    )

    // Method: give initial values
    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeInSeconds.postValue(0L)
        timeInMilli.postValue(0L)
    }

    // Method: create new poly line
    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))


    //Define the location update callback, then add that location to the end of line
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if(isTracking.value!!){
                result?.locations?.let{
                    locations -> for(location in locations){
                        addPathPoint(location)
                    }
                }
            }
        }
    }

    // Method: collect info of current latitude and longitude in the map, then add to the pre point
    // of poly line
    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    //Method: update the current location of user
    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if(isTracking) {
            // checking if has the permission to track
            if(permissionTracking.hasTrackingPermission(this)) {
                val request = LocationRequest().apply {
                    interval = UPDATE_LOCATION_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    // Method: to pause service
    private fun pauseService(){
        isTracking.postValue(false)
        isTimerEnable = false
    }

    // Method: track the actual time
    private fun timer(){
        addEmptyPolyline()
        isTracking.postValue(true)
        timeToggle = System.currentTimeMillis()
        isTimerEnable = true
        // Start or stop the current timer
        CoroutineScope(Dispatchers.Main).launch{
            while(isTracking.value!!){
                lapTime = System.currentTimeMillis() - timeToggle // time difference between now and the last "timer"
                timeInMilli.postValue(totalTime + lapTime) // post the new Lap time
                if(timeInMilli.value!! >= lastSecondTimeStamp + 1000L){
                    timeInSeconds.postValue(timeInSeconds.value!! + 1)
                    lastSecondTimeStamp += 1000L
                }
                delay(50L)
            }
            totalTime += lapTime
        }
    }
}