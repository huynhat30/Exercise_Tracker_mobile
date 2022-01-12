package com.example.exercisetracking.service

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.example.exercisetracking.Contants.PAUSE_SERVICE_ACTION
import com.example.exercisetracking.Contants.START_RESUME_SERVICE_ACTION
import com.example.exercisetracking.Contants.STOP_SERVICE_ACTION

class TrackerService : LifecycleService(){

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if(it.action == START_RESUME_SERVICE_ACTION){
                    Log.d("service_action","Service start/resume")
                }
            else if(it.action == PAUSE_SERVICE_ACTION){
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
}