package com.example.realestatemanager.adapters
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.realestatemanager.activities.LoanCalculatorActivity
import com.example.realestatemanager.fragments.EstateListFragment
import com.example.realestatemanager.fragments.MapViewActivity
import com.example.realestatemanager.fragments.SearchFragment

class ViewPagerAdapter(FragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(FragmentManager,lifecycle){
    override fun getItemCount(): Int {
        return 3
    }
    //viewPager for fragments
    override fun createFragment(position: Int): Fragment{
        return  when(position){
            0->{
                EstateListFragment()
            }
            1->{
              SearchFragment()
            }
            2-> {
                LoanCalculatorActivity()
            }

            else->{
                Fragment()
            }
        }
    }
}