package com.example.exercisetracking

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
/**
 * Created by Nhat Huy Giang
 * Stydent ID: 20313345
 * Email: psyng1@nottingham.ac.uk
 */

@HiltAndroidApp
class BaseApplication : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}