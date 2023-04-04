package com.example.realestatemanager.activities
import androidx.lifecycle.LiveData
import com.example.realestatemanager.model.EstateData
class EstateRepository(private val estateDao: EstateDao){

    val readAllData:LiveData<List<EstateData>> = estateDao.readData()

    suspend fun addEstate(estateData: EstateData) {
        estateDao.addEstate(estateData)
    }
    suspend fun updateEstate(estateData: EstateData) {
        estateDao.updateEstate(estateData)
    }
}