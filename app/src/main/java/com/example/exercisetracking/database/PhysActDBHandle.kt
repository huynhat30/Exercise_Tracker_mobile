package com.example.exercisetracking.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteAbortException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.LiveData

class PhysActDBHandle(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        // Information of Database
        private const val DATABASE_NAME = "PhysActivityDatabase"
        private const val TABLE_NAME = "physAct_table"
        private const val DATABASE_VERSION = 1

        //Define properties for the table
        private const val KEY_ID = "_id" // Unique IDs for table
        private const val COL_TYPE = "Exercise type" // Type of exercise been taken
        private const val COL_SCREENSHOT = "Screenshot moment" //image of the latitude that user did exercises
        private const val COL_DATE = "Date of exercise" // The date taking the exercise
        private const val COL_TIME = "Length of exercise" // Recording time for how long the exercise been taken
        private const val COL_SPEED = "Avg.Speed of user" // Average speed user had in the exercise
        private const val COL_DISTANCE = "Distance of exercise" // The distance user took
        private const val COL_BURNEDCALOR = "Burned calorie" // estimated calorie been burn in exercise
        private const val COL_IMG1 = "(Optional) Moment 1" // Optional taking moment image 1
        private const val COL_IMG2 = "(Optional) Moment 2" // Optional taking moment image 2

        // Create table command
        private const val CREATE_ACTIVITY_TABLE = ("CREATE TABLE" + TABLE_NAME + "("
                                                    + KEY_ID + " INTEGER PRIMARY KEY," + COL_TYPE + " TEXT,"
                                                    + COL_SCREENSHOT + " BLOB," + COL_DATE + " INTEGER," + COL_TIME + " INTEGER,"
                                                    + COL_SPEED + " REAL," + COL_DISTANCE + " INTEGER," + COL_BURNEDCALOR + " INTEGER,"
                                                    + COL_IMG1 + " BLOB," + COL_IMG2 + " BLOB" + ")")
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // Create table in database
        db?.execSQL(CREATE_ACTIVITY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    @Throws(SQLiteException::class)
    fun addActivity(act: PhysActDBModel): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_TYPE, act.exerType)
        contentValues.put(COL_SCREENSHOT, act.img1)
        contentValues.put(COL_DATE, act.dateRecord)
        contentValues.put(COL_TIME, act.timeRecord)
        contentValues.put(COL_SPEED, act.avgVelKMH)
        contentValues.put(COL_DISTANCE, act.distanceMeter)
        contentValues.put(COL_BURNEDCALOR, act.estCalor)
        contentValues.put(COL_IMG1, act.img2)
        contentValues.put(COL_IMG1, act.img3)

        val success = db.insert(TABLE_NAME, null, contentValues)

        db.close()
        return success
    }

    fun getAllExercisesSortedByDate(): ArrayList<PhysActDBModel>{
        val actList: ArrayList<PhysActDBModel> = ArrayList<PhysActDBModel>()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY COL_DATE DESC"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        var id: Int
        var type: String
        var screenShot: ByteArray
        var date: Long
        var time: Long
        var speed: Float
        var distance: Int
        var burnCalor: Int
        var img1: ByteArray
        var img2: ByteArray

        try{
            cursor = db.rawQuery(selectQuery, null)
        }
        catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                type = cursor.getString(cursor.getColumnIndex(COL_TYPE))
                screenShot = cursor.getBlob(cursor.getColumnIndex(COL_SCREENSHOT))
                date = cursor.getLong(cursor.getColumnIndex(COL_DATE))
                time = cursor.getLong(cursor.getColumnIndex(COL_TIME))
                speed = cursor.getFloat(cursor.getColumnIndex(COL_SPEED))
                distance = cursor.getInt(cursor.getColumnIndex(COL_DISTANCE))
                burnCalor = cursor.getInt(cursor.getColumnIndex(COL_BURNEDCALOR))
                img1 = cursor.getBlob(cursor.getColumnIndex(COL_IMG1))
                img2 = cursor.getBlob(cursor.getColumnIndex(COL_IMG2))

                val act = PhysActDBModel(id = id, exerType = type, img1 = screenShot, dateRecord = date, timeRecord = time,
                                        avgVelKMH = speed, distanceMeter = distance, estCalor = burnCalor, img2 = img1, img3 = img2)
                actList.add(act)

            } while (cursor.moveToNext())
        }
        return actList
    }

    fun getAllExercisesSortedByType(): ArrayList<PhysActDBModel>{
        val actList: ArrayList<PhysActDBModel> = ArrayList<PhysActDBModel>()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY COL_TYPE DESC"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        var id: Int
        var type: String
        var screenShot: ByteArray
        var date: Long
        var time: Long
        var speed: Float
        var distance: Int
        var burnCalor: Int
        var img1: ByteArray
        var img2: ByteArray

        try{
            cursor = db.rawQuery(selectQuery, null)
        }
        catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                type = cursor.getString(cursor.getColumnIndex(COL_TYPE))
                screenShot = cursor.getBlob(cursor.getColumnIndex(COL_SCREENSHOT))
                date = cursor.getLong(cursor.getColumnIndex(COL_DATE))
                time = cursor.getLong(cursor.getColumnIndex(COL_TIME))
                speed = cursor.getFloat(cursor.getColumnIndex(COL_SPEED))
                distance = cursor.getInt(cursor.getColumnIndex(COL_DISTANCE))
                burnCalor = cursor.getInt(cursor.getColumnIndex(COL_BURNEDCALOR))
                img1 = cursor.getBlob(cursor.getColumnIndex(COL_IMG1))
                img2 = cursor.getBlob(cursor.getColumnIndex(COL_IMG2))

                val act = PhysActDBModel(id = id, exerType = type, img1 = screenShot, dateRecord = date, timeRecord = time,
                    avgVelKMH = speed, distanceMeter = distance, estCalor = burnCalor, img2 = img1, img3 = img2)
                actList.add(act)

            } while (cursor.moveToNext())
        }
        return actList
    }

    fun getAllExercisesSortedByTime(): ArrayList<PhysActDBModel>{
        val actList: ArrayList<PhysActDBModel> = ArrayList<PhysActDBModel>()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY COL_TIME DESC"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        var id: Int
        var type: String
        var screenShot: ByteArray
        var date: Long
        var time: Long
        var speed: Float
        var distance: Int
        var burnCalor: Int
        var img1: ByteArray
        var img2: ByteArray

        try{
            cursor = db.rawQuery(selectQuery, null)
        }
        catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                type = cursor.getString(cursor.getColumnIndex(COL_TYPE))
                screenShot = cursor.getBlob(cursor.getColumnIndex(COL_SCREENSHOT))
                date = cursor.getLong(cursor.getColumnIndex(COL_DATE))
                time = cursor.getLong(cursor.getColumnIndex(COL_TIME))
                speed = cursor.getFloat(cursor.getColumnIndex(COL_SPEED))
                distance = cursor.getInt(cursor.getColumnIndex(COL_DISTANCE))
                burnCalor = cursor.getInt(cursor.getColumnIndex(COL_BURNEDCALOR))
                img1 = cursor.getBlob(cursor.getColumnIndex(COL_IMG1))
                img2 = cursor.getBlob(cursor.getColumnIndex(COL_IMG2))

                val act = PhysActDBModel(id = id, exerType = type, img1 = screenShot, dateRecord = date, timeRecord = time,
                    avgVelKMH = speed, distanceMeter = distance, estCalor = burnCalor, img2 = img1, img3 = img2)
                actList.add(act)

            } while (cursor.moveToNext())
        }
        return actList
    }

    fun deleteActivity(act: PhysActDBModel): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, act.id)
        val success = db.delete(TABLE_NAME, KEY_ID + "=" + act.id, null)
        db.close()
        return success
    }
}