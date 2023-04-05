package com.example.realestatemanager.adapters
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.realestatemanager.R
import com.example.realestatemanager.model.EstateData
class SearchAdapter: RecyclerView.Adapter<SearchAdapter.SearchHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        return SearchHolder(
            LayoutInflater.from(parent.context).inflate
                (R.layout.estate_list_header,parent,false)
        )
    }

    //Estates which will be displayed in recyclerView
    @SuppressLint("StringFormatInvalid")
    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        val estates = differ.currentList[position]
        holder.view.apply {
            Glide.with(context)
                .load(estates.photoUris[0])
                .error(R.drawable.ic_baseline_error_outline_24)
                .centerCrop()
                .into(holder.estateImage)
            // Type, district x price
            holder.estateType.text = estates.type
            holder.estateDistrict.text = estates.district
            holder.estatePrice.text =estates.price.toString()

        }
    }
    // size
    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    //UI items for recyclerView
    class SearchHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        val estateType: TextView =view.findViewById(R.id.estate_type)
        val estateDistrict: TextView =view.findViewById(R.id.estate_district)
        val estatePrice: TextView =view.findViewById(R.id.estate_price)
        val estateImage: AppCompatImageView =view.findViewById(R.id.estate_photo)
    }
    //DiffUtil for items
    private val differCallBack by lazy {
        object : DiffUtil.ItemCallback<EstateData>() {
        override fun areItemsTheSame(oldItem: EstateData, newItem: EstateData): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: EstateData, newItem: EstateData): Boolean {
            return oldItem == newItem
        }
    }
    }
    val differ = AsyncListDiffer(this, differCallBack)
}