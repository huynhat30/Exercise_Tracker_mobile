package com.example.exercisetracking.service

import java.util.concurrent.TimeUnit

object TimerConverter {

    //Method: get the current time in millisecond then convert to hour/minutes/seconds format
    fun getTimerFormatted(ms: Long, includeMillis: Boolean = false) : String{
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)

        if(!includeMillis){
            return "${if(hours < 10) "0" else ""}$hours:" +
                    "${if(minutes < 10) "0" else ""}$minutes:" +
                    "${if(seconds < 10) "0" else ""}$seconds"
        }
        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliseconds /= 10
        return "${if(hours < 10) "0" else ""}$hours:" +
                "${if(minutes < 10) "0" else ""}$minutes:" +
                "${if(seconds < 10) "0" else ""}$seconds"
    }
}