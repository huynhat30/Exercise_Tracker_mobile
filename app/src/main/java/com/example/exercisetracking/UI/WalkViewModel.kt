package com.example.exercisetracking.UI

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exercisetracking.database.PhysActivity
import com.example.exercisetracking.database.repo.MainRepo
import kotlinx.coroutines.launch

//collect the data from the repo and provide to all fragments/activities
class WalkViewModel @ViewModelInject constructor(val mainRepo : MainRepo) : ViewModel() {
    val walkSortedBydate = mainRepo.getAllWalkSortedByDate()
    val walkSortedByTime = mainRepo.getAllWalkSortedByTime()
    val walkSortedByDistance = mainRepo.getAllWalkSortedByDistance()
    val walkSortedByStep = mainRepo.getAllWalkSortedByStep()

    fun insertRun(walk : PhysActivity) = viewModelScope.launch {
        mainRepo.insertAct(walk)
    }
}