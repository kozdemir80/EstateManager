package com.example.realestatemanager.dao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.realestatemanager.model.EstateData
import com.example.realestatemanager.model.UserData
@Database(entities = [EstateData::class], version = 1, exportSchema = false)
@TypeConverters(EstateData.MyVicinityConvertor::class)
abstract class EstateDataBase: RoomDatabase() {
    abstract fun estateDao(): EstateDao
    companion object {
        @Volatile
        private var INSTANCE: EstateDataBase? = null
        fun getDatabase(context: Context): EstateDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EstateDataBase::class.java,
                    "estate_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
    @Database(entities = [UserData::class], version = 1, exportSchema = false)
    abstract class UserDataBase:RoomDatabase() {
        abstract fun userDao(): UserDao
        companion object{
            @Volatile
            private var INSTANCE: UserDataBase?=null
            fun getUserDatabase(context: Context): UserDataBase {
                val tempInstance = INSTANCE
                if (tempInstance != null) {
                    return tempInstance
                }
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDataBase::class.java,
                        "user_database"
                    ).build()
                    INSTANCE = instance
                    return instance
                }
            }
            }
        }
    }
