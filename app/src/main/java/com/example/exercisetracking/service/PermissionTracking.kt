package com.example.exercisetracking.UI.tracking

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Build
import com.example.exercisetracking.service.Polyline
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by Nhat Huy Giang
 * Stydent ID: 20313345
 * Email: psyng1@nottingham.ac.uk
 */
object permissionTracking {

    // Method: in case the device has had the location permission ->
    // check the Android version, versions below Q do not need foreground permission
    fun hasTrackingPermission(context: Context) =
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        else{
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

    //Method: calculate the total distance of a run
    fun calculatePolylineLength(polyline: Polyline) : Float{
        var dis = 0f
        for(i in 0..polyline.size - 2){
            val pos1 = polyline[i]
            val pos2 = polyline[i + 1]

            val result = FloatArray(1)
            Location.distanceBetween(pos1.latitude, pos1.longitude, pos2.latitude, pos2.longitude,result)
            dis += result[0]
        }
        return dis
    }
}