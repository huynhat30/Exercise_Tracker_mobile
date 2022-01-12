package com.example.exercisetracking.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DataRecordDAO {

    // Method: Replace the old exercise with new one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(run: PhysActivity)

    // Method: Delete an exercise
    @Delete
    suspend fun deleteExercise(run: PhysActivity)

    // Methods: get data sorted by table properties
    @Query("SELECT * FROM physAct_table ORDER BY dateRecord DESC")
    fun getAllExercisesSortedByDate(): LiveData<List<PhysActivity>>

    @Query("SELECT * FROM physAct_table ORDER BY exerType DESC")
    fun getAllExercisesSortedByType(): LiveData<List<PhysActivity>>

    @Query("SELECT * FROM physAct_table ORDER BY timeRecord DESC")
    fun getAllExercisesSortedByTime(): LiveData<List<PhysActivity>>

    @Query("SELECT * FROM physAct_table ORDER BY latitudeMeter DESC")
    fun getAllExercisesSortedByDistance(): LiveData<List<PhysActivity>>

    @Query("SELECT * FROM physAct_table ORDER BY avgVelKMH DESC")
    fun getAllExercisesSortedBySpeed(): LiveData<List<PhysActivity>>

    // Methods: get Sum of some properties
    @Query("SELECT SUM(estCalor) FROM physAct_table")
    fun getSumOfBurnedCalor(): LiveData<Long>

    @Query("SELECT SUM(latitudeMeter) FROM physAct_table")
    fun getSumOfDoneDistance(): LiveData<Long>

}