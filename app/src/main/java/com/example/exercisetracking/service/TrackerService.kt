package com.example.exercisetracking.service

import android.content.Intent
import androidx.lifecycle.LifecycleService

class TrackerService : LifecycleService(){
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}