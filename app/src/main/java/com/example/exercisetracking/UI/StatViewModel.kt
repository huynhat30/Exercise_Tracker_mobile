package com.example.exercisetracking.UI

import androidx.lifecycle.ViewModel
import com.example.exercisetracking.database.repo.MainRepo
import dagger.hilt.android.scopes.ViewModelScoped

//collect the data from the repo and provide to all fragments/activities
class StatViewModel @ViewModelScoped constructor(val mainRepo : MainRepo) : ViewModel() {

}