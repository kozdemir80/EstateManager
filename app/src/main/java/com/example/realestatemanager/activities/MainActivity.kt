package com.example.realestatemanager.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.realestatemanager.R
import com.example.realestatemanager.adapters.ViewPagerAdapter
import com.example.realestatemanager.constants.Constants.Companion.channelId
import com.example.realestatemanager.databinding.ActivityMainBinding
import com.example.realestatemanager.fragments.MapViewFragment
import com.example.realestatemanager.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter= ViewPagerAdapter(supportFragmentManager,lifecycle)
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController=binding.fragmentContainerView.getFragment<NavHostFragment>().navController
        toggle= ActionBarDrawerToggle(this,binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.bringToFront()
        binding.viewPager.adapter=adapter
        bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.search_view->binding.viewPager.currentItem=0
                R.id.map_view->binding.viewPager.currentItem=1
            }
            true
        }


    }

    // Create notification channel so that user receives notification after creating new estate
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_title)
            val descriptionText = getString(R.string.notification_channel_description)
            val channel = NotificationChannel(
                channelId,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}