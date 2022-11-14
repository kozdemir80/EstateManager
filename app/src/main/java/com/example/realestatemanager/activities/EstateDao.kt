package com.example.realestatemanager.activities

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.realestatemanager.model.EstateData

@Dao
interface EstateDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEstate (estateData:EstateData)
    @Query("SELECT * FROM estate_table ORDER BY id ASC")
    fun readData():LiveData<List<EstateData>>

}