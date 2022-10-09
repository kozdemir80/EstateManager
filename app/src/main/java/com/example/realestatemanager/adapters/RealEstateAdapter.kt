package com.example.realestatemanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.realestatemanager.R
import com.example.realestatemanager.model.EstateData

class RealEstateAdapter:RecyclerView.Adapter<RealEstateAdapter.ListViewHolder>(){



    private var estateList = emptyList<EstateData>()
    private lateinit var context: Context



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        context = parent.context
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate
                (R.layout.estate_list_activity,parent,false)
        )

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = estateList[position]


    }
    class ListViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    }
    override fun getItemCount(): Int {
        return estateList.size
    }
}