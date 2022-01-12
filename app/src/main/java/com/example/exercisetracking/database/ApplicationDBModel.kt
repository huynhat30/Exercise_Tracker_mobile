package com.example.exercisetracking.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [PhysActivity::class],
    version = 1
)

@TypeConverters(PropertyDBConverter::class)
abstract class ApplicationDBModel: RoomDatabase() {
    abstract fun getDataRecordDAO(): DataRecordDAO
}