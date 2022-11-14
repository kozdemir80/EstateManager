package com.example.realestatemanager.activities

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.realestatemanager.model.EstateData

@Database(entities = [EstateData::class], version = 1, exportSchema = false)
abstract class EstateDatabase: RoomDatabase() {
    abstract fun estateDao():EstateDao

    companion object{
        @Volatile
        private var INSTANCE:EstateDatabase? = null
        fun getDatabase(context: Context):EstateDatabase{
            val tempInstance= INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    EstateDatabase::class.java,
                    "estate_database"
                ).build()
                INSTANCE=instance
                return instance
            }
        }
    }
}