package com.example.exercisetracking.UI.Fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.exercisetracking.Contants.CODE_LOCATION_PERMISSION_REQUAEST
import com.example.exercisetracking.R
import com.example.exercisetracking.UI.AppViewModel
import com.example.exercisetracking.UI.tracking.permissionTracking
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_running.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint // inject request data through Dagger Hilt into Fragment
class WalkingFragment: Fragment(R.layout.fragment_walking ), EasyPermissions.PermissionCallbacks{
    private val viewModel : AppViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
        fab.setOnClickListener{
            findNavController().navigate(R.id.action_runningFragment_to_trackingFragment)
        }
    }

    //Method: check and send request to access permission
    private fun requestPermission(){
        // check if user has accepted permission -> run the app
        if(permissionTracking.hasTrackingPermission(requireContext())){
            return
        }

        // check if user has not accepted permission -> check android version -> ask for permission
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(this, "Accept location permission before using this app", CODE_LOCATION_PERMISSION_REQUAEST,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        else{
            EasyPermissions.requestPermissions(this, "Accept location permission before using this app", CODE_LOCATION_PERMISSION_REQUAEST,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    //Methods: on accept or deny permission from user
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

        // In case, user permanently denied the permission, go to device setting
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
        }

        // In case, user denied the permission 1 time, request permission again
        else{
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}