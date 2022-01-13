package com.example.exercisetracking.database

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Nhat Huy Giang
 * Stydent ID: 20313345
 * Email: psyng1@nottingham.ac.uk
 *
 * Class: Create table in database
 * */
@Entity(tableName = "physAct_table")
data class PhysActivity (

    //Define properties for the table
    var exerType: String, // Type of exercise been taken
    var img1: Bitmap? = null, //image of the latitude that user did exercises
    var dateRecord: Long = 0L, // The date taking the exercise
    var timeRecord: Long = 0L, // Recording time for how long the exercise been taken
    var avgVelKMH: Float = 0f, // Average speed user had in the exercise
    var step: Int = 0, // Step in a walk
    var latitudeMeter: Int = 0, // The distance user took
    var estCalor: Int = 0 // estimated calorie been burn in exercise
) {
    // Adding primary key for the table
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}