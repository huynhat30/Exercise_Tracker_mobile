package com.example.exercisetracking.database.repo

import androidx.lifecycle.LiveData
import com.example.exercisetracking.database.DataRecordDAO
import com.example.exercisetracking.database.PhysActivity
import javax.inject.Inject

/**
 * Created by Nhat Huy Giang
 * Stydent ID: 20313345
 * Email: psyng1@nottingham.ac.uk
 *
 * class: read and temporary save requested data from Database, connect between viewmodel and database
 */

class MainRepo @Inject constructor(val actDao: DataRecordDAO) {

    suspend fun insertAct(act: PhysActivity) = actDao.insertExercise(act)

    suspend fun deleteAct(act: PhysActivity) = actDao.deleteExercise(act)

    fun getAllRunSortedByDate() = actDao.getAllRunSortedByDate()

    fun getAllRunSortedByTime() = actDao.getAllRunSortedByTime()

    fun getAllRunSortedByDistance() = actDao.getAllRunSortedByDistance()

    fun getAllRunSortedBySpeed() = actDao.getAllRunSortedBySpeed()



    fun getAllWalkSortedByDate() = actDao.getAllWalkSortedByDate()

    fun getAllWalkSortedByTime() = actDao.getAllWalkSortedByTime()

    fun getAllWalkSortedByDistance() = actDao.getAllWalkSortedByDistance()

    fun getAllWalkSortedByStep() = actDao.getAllWalkSortedByStep()



    fun getSumOfBurnedCalor() = actDao.getSumOfBurnedCalor()

    fun getSumOfDoneDistance() = actDao.getSumOfDoneDistance()

    fun getSumOfStep() = actDao.getSumOfStep()


}