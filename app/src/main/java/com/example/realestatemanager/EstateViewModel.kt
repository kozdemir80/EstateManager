package com.example.realestatemanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.activities.EstateDatabase
import com.example.realestatemanager.activities.EstateRepository
import com.example.realestatemanager.model.EstateData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstateViewModel(application: Application):AndroidViewModel(application){
    private val readAllData:LiveData<List<EstateData>>
    private val repository:EstateRepository
    init {
        val estateDao=EstateDatabase.getDatabase(application).estateDao()
        repository= EstateRepository(estateDao)
        readAllData= repository.readAllData
    }

    fun addEstate(estateData: EstateData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addEstate(estateData)
        }
    }
}