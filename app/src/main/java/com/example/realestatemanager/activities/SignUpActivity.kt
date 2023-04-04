package com.example.realestatemanager.activities
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanager.EstateViewModel
import com.example.realestatemanager.R
import com.example.realestatemanager.databinding.SignUpActivityBinding
import com.example.realestatemanager.model.UserData
import com.google.firebase.database.*
class SignUpActivity:AppCompatActivity(){
    private lateinit var binding: SignUpActivityBinding
    private var checkUser = false
    private lateinit var estateViewModel: EstateViewModel
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_activity)
        binding = SignUpActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        estateViewModel = ViewModelProvider(this)[EstateViewModel::class.java]
        addUser()
        rememberUser()
    }
    //Create a user to sign-in
    private fun addUser(){
        val preferences=getSharedPreferences("saveUser", MODE_PRIVATE)
        val editor=preferences.edit()
        binding.signUp.setOnClickListener{
            checkUser=checkUser()
            if (checkUser){
                val userData=UserData(
                    id = 0,
                    username = binding.userName.text.toString(),
                    password = binding.password.text.toString()
                )
                binding.checkBox.setOnClickListener {
                if (binding.checkBox.isChecked){
                editor.putString("UserName",binding.userName.text.toString())
                editor.putString("password",binding.password.text.toString())
                editor.apply()}}
                Toast.makeText(applicationContext,"Successfully Registered",Toast.LENGTH_SHORT).show()
                estateViewModel.addUser(userData)
                val intent=Intent(this,AddRealEstateActivity::class.java)
                startActivity(intent)
            }
        }
    }
    //Checking if user fields are filled
    private fun checkUser():Boolean{
        binding.apply {
            if (userName.text!!.isEmpty()){
                userName.error="Username is required"
                return false
            }else {
                userName.error = null
            }
            if (password.text!!.isEmpty()){
                password.error="Password is required"
                return false
            }else{
                password.error=null
            }
        }
        return true
    }
    //Remembering userName and password method
    private fun rememberUser(){
        val preferences=getSharedPreferences("saveUser", MODE_PRIVATE)
                val user= preferences.getString("UserName",null)
                val password=preferences.getString("password",null)
                binding.userName.setText(user).toString()
                binding.password.setText(password).toString()
    }
}