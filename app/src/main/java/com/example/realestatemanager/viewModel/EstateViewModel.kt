package com.example.realestatemanager.viewModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.dao.EstateDataBase
import com.example.realestatemanager.dao.EstateDataBase.Companion.getDatabase
import com.example.realestatemanager.dao.EstateRepository
import com.example.realestatemanager.dao.UserRepository
import com.example.realestatemanager.model.EstateData
import com.example.realestatemanager.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class EstateViewModel(application: Application):AndroidViewModel(application){
    //--- VİEW MODEL FOR REAL ESTATE ---
     val readAllData:LiveData<List<EstateData>>
    private val repository: EstateRepository
    init {
        val estateDao=getDatabase(application).estateDao()
        repository= EstateRepository(estateDao)
        readAllData= repository.readAllData
    }

    fun addEstate(estate:EstateData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addEstate(estate)
        }
    }
    fun updateEstate(estate: EstateData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEstate(estate)
        }
    }
    // --- VİEW MODEL FOR USER ---
    val readUserData:LiveData<List<UserData>>
    private val userRepository: UserRepository
    init {
        val userDao= EstateDataBase.UserDataBase.getUserDatabase(application).userDao()
        userRepository= UserRepository(userDao)
        readUserData=userRepository.readUserData
    }
    fun addUser(userData: UserData){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addUser(userData)
        }
    }
}