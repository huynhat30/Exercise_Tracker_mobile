package com.example.exercisetracking.database

import android.graphics.Bitmap

class PhysActDBModel(val id: Int, val exerType: String, val img1: ByteArray, val dateRecord: Long, val timeRecord: Long,
                     val avgVelKMH: Float, val distanceMeter: Int, val estCalor: Int, val img2: ByteArray,
                     val img3: ByteArray)
{}