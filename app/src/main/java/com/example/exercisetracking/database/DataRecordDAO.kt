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
    @Query("SELECT * FROM physAct_table WHERE exerType = 'Run' ORDER BY dateRecord DESC")
    fun getAllRunSortedByDate(): LiveData<List<PhysActivity>>

    @Query("SELECT * FROM physAct_table WHERE exerType = 'Run' ORDER BY timeRecord DESC")
    fun getAllRunSortedByTime(): LiveData<List<PhysActivity>>

    @Query("SELECT * FROM physAct_table WHERE exerType = 'Run' ORDER BY latitudeMeter DESC")
    fun getAllRunSortedByDistance(): LiveData<List<PhysActivity>>

    @Query("SELECT * FROM physAct_table WHERE exerType = 'Run' ORDER BY avgVelKMH DESC")
    fun getAllRunSortedBySpeed(): LiveData<List<PhysActivity>>



    @Query("SELECT * FROM physAct_table WHERE exerType = 'Walk' ORDER BY dateRecord DESC")
    fun getAllWalkSortedByDate(): LiveData<List<PhysActivity>>

    @Query("SELECT * FROM physAct_table WHERE exerType = 'Walk' ORDER BY timeRecord DESC")
    fun getAllWalkSortedByTime(): LiveData<List<PhysActivity>>

    @Query("SELECT * FROM physAct_table WHERE exerType = 'Walk' ORDER BY latitudeMeter DESC")
    fun getAllWalkSortedByDistance(): LiveData<List<PhysActivity>>

    @Query("SELECT * FROM physAct_table WHERE exerType = 'Walk' ORDER BY step DESC")
    fun getAllWalkSortedByStep(): LiveData<List<PhysActivity>>



    // Methods: get Sum of some properties
    @Query("SELECT SUM(estCalor) FROM physAct_table")
    fun getSumOfBurnedCalor(): LiveData<Long>

    @Query("SELECT SUM(latitudeMeter) FROM physAct_table")
    fun getSumOfDoneDistance(): LiveData<Long>

    @Query("SELECT SUM(step) FROM physAct_table")
    fun getSumOfStep(): LiveData<Long>

}