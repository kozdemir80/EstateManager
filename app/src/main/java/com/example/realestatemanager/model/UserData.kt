package com.example.realestatemanager.model
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users_table")
@Parcelize
data class UserData(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val username: String,
    val password: String):Parcelable
