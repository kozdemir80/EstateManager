package com.example.realestatemanager.adapters
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.realestatemanager.databinding.PagerItemBinding
class PhotoAdapter(private val uris: ArrayList<Uri>,
                   private val captions: ArrayList<String>,
                   private val context:Context
): RecyclerView.Adapter<PhotoAdapter.PagerViewHolder>(){
    inner class PagerViewHolder(val binding: PagerItemBinding) :
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoAdapter.PagerViewHolder {
        return PagerViewHolder(
            PagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    override fun onBindViewHolder(holder: PhotoAdapter.PagerViewHolder, position: Int) {
        holder.binding.apply {
            // Display photo
            Glide.with(context)
                .load(uris[position])
                .centerCrop()
                .into(pagerImage)
            // Set caption
            pagerCaption.text = captions[position]
        }
    }
    override fun getItemCount(): Int {
        return uris.size
    }
}