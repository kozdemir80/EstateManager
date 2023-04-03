package com.example.realestatemanager.activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.realestatemanager.EstateViewModel
import com.example.realestatemanager.R
import com.example.realestatemanager.adapters.ViewPagerAdapter
import com.example.realestatemanager.databinding.ActivityMainBinding
import com.example.realestatemanager.fragments.MapViewActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var estateViewModel:EstateViewModel
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
                R.id.list->binding.viewPager.currentItem=0
                R.id.search_view->binding.viewPager.currentItem=1
                R.id.loan_calculator->binding.viewPager.currentItem=2
            }
            true
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        when (item.itemId) {
            R.id.add_estate_info -> {
                estateViewModel= ViewModelProvider(this)[EstateViewModel::class.java]
                estateViewModel.readUserData.observe(this) { users ->
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (users.isEmpty()) {
                            val signInIntent = Intent(this, SignUpActivity::class.java)
                            startActivity(signInIntent)
                            finish()
                        }else{
                            Handler(Looper.getMainLooper()).postDelayed({
                                if (users.isNotEmpty()) {
                                    val intent = Intent(this, AddRealEstateActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }, 2000)
                        }
                    }, 2000)

                }
            }
          R.id.sign_out-> {
              val intent = Intent(this, SignUpActivity::class.java)
              startActivity(intent)
          }
          R.id.mapView->{
              estateViewModel= ViewModelProvider(this)[EstateViewModel::class.java]
              estateViewModel.readUserData.observe(this) { users ->
                  Handler(Looper.getMainLooper()).postDelayed({
                      if (users.isEmpty()) {
                          val signInIntent = Intent(this, SignUpActivity::class.java)
                          startActivity(signInIntent)
                          finish()
                      }else{
                          Handler(Looper.getMainLooper()).postDelayed({
                              if (users.isNotEmpty()) {
                                  val intent = Intent(this, MapViewActivity::class.java)
                                  startActivity(intent)
                                  finish()
                              }
                          }, 2000)
                      }
                  }, 2000)
          }
        }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_user,menu)
        return super.onCreateOptionsMenu(menu)
    }
}