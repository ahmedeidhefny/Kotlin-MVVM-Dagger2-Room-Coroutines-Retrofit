package com.example.android_skills.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_skills.model.Exhibit
import com.example.android_skills.model.ExhibitsLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel architecture component implementation:
//      here we're gonna retrieve data through viewModelScope.
//      besides VM usage is profitably since ViewModel-class keeps alive and saves all processes
//      during configuration changes (e.g. screen rotation)

class DaggerViewModel @Inject constructor(
    private val loader: ExhibitsLoader
) : ViewModel() {
    private val _exhibitsListMutable = MutableLiveData<ArrayList<Exhibit>>()
    private lateinit var viewModelJob: Job

    init {
        loadData()
    }

    private fun loadData() {
        viewModelJob = viewModelScope.launch {

            val artists = loader.getExhibitList()
            _exhibitsListMutable.postValue(ArrayList(artists))
        }
    }

    fun getExhibitsList(): LiveData<ArrayList<Exhibit>> = _exhibitsListMutable

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}