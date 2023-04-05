package com.example.realestatemanager.adapters
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.realestatemanager.R
import com.example.realestatemanager.databinding.PhotoItemBinding
class GridAdapter(
    private val uris: ArrayList<Uri>,
    private val captions: ArrayList<String>,
    private val context: Context
) :
    RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    inner class GridViewHolder(val binding: PhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        return GridViewHolder(
            PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.binding.apply {
            // Display photo
            Glide.with(context)
                .load(uris[position])
                .error(R.drawable.ic_baseline_error_outline_24)
                .centerCrop()
                .into(photoImage)
            // Set caption
            photoCaption.text = captions[position]
            // Remove item
            photoDeleteBtn.setOnClickListener {
                uris.removeAt(holder.adapterPosition)
                captions.removeAt(holder.adapterPosition)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, uris.size)
                notifyItemRangeChanged(position, captions.size)
            }
        }
    }
    override fun getItemCount(): Int {
        return uris.size
    }
}