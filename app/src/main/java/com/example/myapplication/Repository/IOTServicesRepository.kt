package com.example.myapplication.Repository

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Service
import il.co.syntax.finalkotlinproject.utils.Resource

interface IOTServicesRepository {
    fun swapStatus(servicenumber: String)
    fun getAllTasksLiveData(data : MutableLiveData<Resource<List<Service>>>)
}