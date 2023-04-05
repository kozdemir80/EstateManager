package com.example.realestatemanager.activities
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.realestatemanager.model.UserData
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(userData: UserData)
    @Query("SELECT * FROM users_table ORDER BY id ASC")
    fun readUser(): LiveData<List<UserData>>
}