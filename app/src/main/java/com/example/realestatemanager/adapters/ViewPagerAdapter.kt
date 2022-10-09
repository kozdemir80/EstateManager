package com.example.realestatemanager.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.realestatemanager.fragments.MapViewFragment
import com.example.realestatemanager.fragments.SearchFragment

class ViewPagerAdapter(FragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(FragmentManager,lifecycle){
    override fun getItemCount(): Int {
        return 2
    }
    //viewPager for fragments
    override fun createFragment(position: Int): Fragment {
        return  when(position){
            0->{
              SearchFragment()
            }
            1->{
                MapViewFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}