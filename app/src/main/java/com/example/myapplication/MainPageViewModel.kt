package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Repository.IOTServicesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import il.co.syntax.finalkotlinproject.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(private val serviceRepositoy: IOTServicesRepository) : ViewModel() {
    private val instance = serviceRepositoy
    val _eventsStatus : MutableLiveData<Resource<List<Service>>> = MutableLiveData()
    val eventsStatus : LiveData<Resource<List<Service>>> = _eventsStatus

    init {
        viewModelScope.launch {
            instance.getAllTasksLiveData(_eventsStatus)
        }
    }

    fun updateService(servicenumber: String) {
        viewModelScope.launch {
            serviceRepositoy.swapStatus(servicenumber)
        }
    }
}