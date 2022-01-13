package com.example.exercisetracking.UI

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.exercisetracking.database.repo.MainRepo

/**
 * Created by Nhat Huy Giang
 * Stydent ID: 20313345
 * Email: psyng1@nottingham.ac.uk
 *
 * Class describe: This class collect the data from the repo and provide to all fragments/activities
 * */
class StatViewModel @ViewModelInject constructor(val mainRepo : MainRepo) : ViewModel() {
    val totalDistance = mainRepo.getSumOfDoneDistance()
    val totalCalorBurned = mainRepo.getSumOfBurnedCalor()
}