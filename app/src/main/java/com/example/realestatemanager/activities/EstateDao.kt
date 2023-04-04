package com.example.realestatemanager.activities

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.realestatemanager.model.EstateData
@Dao
interface EstateDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEstate (estateData: EstateData)
    @Query("SELECT * FROM estate_table ORDER BY id ASC")
    fun readData():LiveData<List<EstateData>>
    @Update
    suspend fun updateEstate(estateData: EstateData)
    // --- FOR CONTENT PROVIDER ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEstateForContentProvider(estateData: EstateData): Long
    @Update
    fun updateEstateForContentProvider(estateData: EstateData): Int
    @Query("SELECT * FROM estate_table ORDER BY id ASC")
    fun readAllDataWithCursor(): Cursor
}