package com.example.realestatemanager.activities
import androidx.lifecycle.LiveData
import com.example.realestatemanager.model.UserData
class UserRepository(private val userDao: UserDao){
    val readUserData: LiveData<List<UserData>> = userDao.readUser()
    suspend fun addUser(userData: UserData){
        userDao.addUser(userData)
    }
}