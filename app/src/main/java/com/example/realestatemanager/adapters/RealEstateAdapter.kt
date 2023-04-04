package com.example.realestatemanager.adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.realestatemanager.R
import com.example.realestatemanager.model.EstateData
class RealEstateAdapter:RecyclerView.Adapter<RealEstateAdapter.ListViewHolder>() {
    private lateinit var context: Context
    private lateinit var mListener: OnItemClickListener
    private var estateList = emptyList<EstateData>()

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        context = parent.context
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate
                (R.layout.estate_list_header, parent, false), mListener
        )
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = estateList[position]
        holder.view.apply {
            Glide.with(context)
                .load(currentItem.photoUris[0])
                .error(R.drawable.ic_baseline_error_outline_24)
                .centerCrop()
                .into(holder.estateImage)
            holder.estateDistrict.text = currentItem.address
            holder.estatePrice.text = currentItem.price.toString()
            holder.estateType.text = currentItem.type
            // Display ribbon if estate has been sold
            if (currentItem.status == context.resources.getStringArray(R.array.status)[1]) {
                holder.soldImage.visibility = View.VISIBLE
            }
        }
    }
    class ListViewHolder(val view: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(view) {
        val estateImage: AppCompatImageView = view.findViewById(R.id.estate_photo)
        val estateType: TextView = view.findViewById(R.id.estate_type)
        val estateDistrict: TextView = view.findViewById(R.id.estate_district)
        val estatePrice: TextView = view.findViewById(R.id.estate_price)
        val soldImage:AppCompatImageView=view.findViewById(R.id.estate_ribbon)
        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
    override fun getItemCount(): Int {
        return estateList.size
    }
  @SuppressLint("NotifyDataSetChanged")
  fun setData(estate:List<EstateData>){
      this.estateList=estate
      notifyDataSetChanged()
  }
}