package com.example.exercisetracking.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.ByteArrayOutputStream


/**
 * Created by Nhat Huy Giang
 * Stydent ID: 20313345
 * Email: psyng1@nottingham.ac.uk
 */
object PropertyDBConverter {

    // Method: convert Bitmap value(such sa image) to bytes for saving into DB
    @TypeConverter
    fun fromBitmap(bmp: Bitmap): ByteArray{
         val outputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    // Method: convert bytes values to Bitmap for displaying from DB to Activity
    @TypeConverter
    fun fromByte(image: ByteArray): Bitmap{
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }
}