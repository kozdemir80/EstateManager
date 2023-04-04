package com.example.realestatemanager.activities
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.realestatemanager.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class LoginActivity:AppCompatActivity(){
    private lateinit var userName:TextInputEditText
    private lateinit var password:TextInputEditText
    private lateinit var auth:FirebaseAuth
    private lateinit var signUpText:TextView
    private lateinit var login:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpText= TextView(this)
        signUpText.findViewById<TextView>(R.id.signUpText)
        auth=FirebaseAuth.getInstance()
        setContentView(R.layout.login_activity)
        login()
        signUpText.setOnClickListener {
            val intent=Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    private fun login(){
        userName= TextInputEditText(this)
        userName.findViewById<TextInputEditText>(R.id.userName1)
        password= TextInputEditText(this)
        password.findViewById<TextInputEditText>(R.id.password1)
        login=Button(this)
        login.findViewById<Button>(R.id.login)
        val currentUser=userName.text.toString()
        val userPassword=password.text.toString()
        if (currentUser.isNotEmpty() && userPassword.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(currentUser,userPassword).addOnCompleteListener(this@LoginActivity) { task ->
                    if (task.isSuccessful){
                        val intent=Intent(this@LoginActivity, AddRealEstateActivity::class.java)
                        startActivity(intent)
                    }
                    }
                }catch (e:java.lang.Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@LoginActivity, e.message , Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}