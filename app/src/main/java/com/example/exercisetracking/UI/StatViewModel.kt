package com.example.exercisetracking.UI

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.exercisetracking.database.repo.MainRepo

//collect the data from the repo and provide to all fragments/activities
class StatViewModel @ViewModelInject constructor(val mainRepo : MainRepo) : ViewModel() {
    val totalDistance = mainRepo.getSumOfDoneDistance()
    val totalCalorBurned = mainRepo.getSumOfBurnedCalor()
}