package com.example.exercisetracking.database.repo

import com.example.exercisetracking.database.DataRecordDAO
import com.example.exercisetracking.database.PhysActivity
import javax.inject.Inject

// read and temporary save requested data from Database, connect between viewmodel and database
class MainRepo @Inject constructor(val actDao: DataRecordDAO) {

    suspend fun insertAct(act: PhysActivity) = actDao.insertExercise(act)

    suspend fun deleteAct(act: PhysActivity) = actDao.deleteExercise(act)

    fun getAllExercisesSortedByDate() = actDao.getAllExercisesSortedByDate()

    fun getAllExercisesSortedByType() = actDao.getAllExercisesSortedByType()

    fun getAllExercisesSortedByTime() = actDao.getAllExercisesSortedByTime()

    fun getAllExercisesSortedByDistance() = actDao.getAllExercisesSortedByDistance()

    fun getAllExercisesSortedBySpeed() = actDao.getAllExercisesSortedBySpeed()

    fun getSumOfBurnedCalor() = actDao.getSumOfBurnedCalor()

    fun getSumOfDoneDistance() = actDao.getSumOfDoneDistance()

}