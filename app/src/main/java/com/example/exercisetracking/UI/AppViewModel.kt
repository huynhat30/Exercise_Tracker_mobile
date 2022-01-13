package com.example.exercisetracking.UI

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exercisetracking.RunSortType
import com.example.exercisetracking.database.PhysActivity
import com.example.exercisetracking.database.repo.MainRepo
import kotlinx.coroutines.launch

/**
 * Created by Nhat Huy Giang
 * Stydent ID: 20313345
 * Email: psyng1@nottingham.ac.uk
 *
 * Class describe: This class collect the data from the repo and provide to all fragments/activities
* */
class AppViewModel @ViewModelInject constructor(val mainRepo : MainRepo) : ViewModel() {
    private val runSortedBydate = mainRepo.getAllRunSortedByDate()
    private val runSortedByTime = mainRepo.getAllRunSortedByTime()
    private val runSortedByDistance = mainRepo.getAllRunSortedByDistance()
    private val runSortedBySpeed = mainRepo.getAllRunSortedBySpeed()

    val runActs = MediatorLiveData<List<PhysActivity>>()

    var runSortType = RunSortType.DATE

    // Method: view default of runs is sorted by date
    init{
        runActs.addSource(runSortedBydate){ result ->
            if(runSortType == RunSortType.DATE){
                result.let{ runActs.value = it}
            }
        }
        runActs.addSource(runSortedByTime){ result ->
            if(runSortType == RunSortType.RUN_TIME){
                result.let{ runActs.value = it}
            }
        }
        runActs.addSource(runSortedByDistance){ result ->
            if(runSortType == RunSortType.DISTANCE){
                result.let{ runActs.value = it}
            }
        }
        runActs.addSource(runSortedBySpeed){ result ->
            if(runSortType == RunSortType.SPEED){
                result.let{ runActs.value = it}
            }
        }
    }

    // Method: insert new run into DB
    fun insertRun(run : PhysActivity) = viewModelScope.launch {
        mainRepo.insertAct(run)
    }

    // Method: sort run data by categories
    fun sortRuns(sortType : RunSortType) = when(sortType){
        RunSortType.DATE -> runSortedBydate.value?.let{runActs.value = it}
        RunSortType.RUN_TIME -> runSortedByTime.value?.let{runActs.value = it}
        RunSortType.DISTANCE -> runSortedByDistance.value?.let{runActs.value = it}
        RunSortType.SPEED -> runSortedBySpeed.value?.let{runActs.value = it}
    }.also{this.runSortType = sortType}
}