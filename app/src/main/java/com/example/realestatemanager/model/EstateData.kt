package com.example.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estate_table")
data class EstateData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var type: String,
    var district: String,
    var price: Int,
    var description: String,
    var surface: Int,
    var realtor: String,
    var status: String,
    var rooms: Int,
    var bedrooms: Int,
    var bathrooms: Int,
    var address: String,
    var entryDate: String,
    var saleDate: String,
    var vicinity: ArrayList<String>,
    var photoUris: ArrayList<String>,
    var photoCaptions: ArrayList<String>
)