package com.example.realestatemanager.activities
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.realestatemanager.R
import com.example.realestatemanager.adapters.PhotoAdapter
import com.example.realestatemanager.databinding.DescriptionActivityBinding
@Suppress("DEPRECATION")
class DescriptionActivity :AppCompatActivity(){
    private val args by navArgs<DescriptionActivityArgs>()
    private lateinit var binding: DescriptionActivityBinding
    private val photoUris = ArrayList<Uri>()
    private var photoCaptions = ArrayList<String>()
    private lateinit var adapter: PhotoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.description_activity)
        binding=DescriptionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        slider()
        descriptionDetails()
        //Transfer Estate details for updating
       binding.detailFab.setOnClickListener {
           val intent = Intent(this, EditDetailsActivity::class.java)
           intent.putExtra("id",args.estateArgs!!.id).toString()
           intent.putExtra("description", args.estateArgs!!.description)
           intent.putExtra("type", args.estateArgs!!.type)
           intent.putExtra("price", args.estateArgs!!.price.toString())
           intent.putExtra("surface", args.estateArgs!!.surface.toString())
           intent.putExtra("rooms", args.estateArgs!!.rooms.toString())
           intent.putExtra("bedrooms", args.estateArgs!!.bedrooms.toString())
           intent.putExtra("bathrooms", args.estateArgs!!.bathrooms.toString())
           intent.putExtra("realtor", args.estateArgs!!.realtor)
           intent.putExtra("status", args.estateArgs!!.status)
           intent.putExtra("entryDate", args.estateArgs!!.entryDate)
           intent.putExtra("saleDate", args.estateArgs!!.saleDate)
           intent.putExtra("vicinity", args.estateArgs!!.vicinity.toString())
           intent.putExtra("address", args.estateArgs!!.address)
           intent.putExtra("district", args.estateArgs!!.district)
           startActivity(intent)
       }
}
    // Display estate photos w/ slider
    private fun slider() {
        // Get uris
        for (strUri in args.estateArgs!!.photoUris) {
            val uri = Uri.parse(strUri)
            photoUris.add(uri)
        }
        // Get captions
        photoCaptions = args.estateArgs!!.photoCaptions
        // Init ViewPager
        val viewPager = binding.detailPager
        adapter = PhotoAdapter(photoUris, photoCaptions,this)
        viewPager.adapter = adapter
        // Register for page change callback
        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                @SuppressLint("SetTextI18n")
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // Update page number
                    binding.detailPageCounter.text = "${position + 1} / ${photoUris.size}"
                }
            }
        )
    }
    // Details of an estate
  private fun descriptionDetails() {
      binding.detailDescription.text = args.estateArgs!!.description
      binding.detailSurface.text = args.estateArgs!!.surface.toString()
      binding.detailRooms.text = args.estateArgs!!.rooms.toString()
      binding.detailBedrooms.text = args.estateArgs!!.bedrooms.toString()
      binding.detailBathrooms.text = args.estateArgs!!.bathrooms.toString()
      binding.detailRealtor.text = args.estateArgs!!.realtor
      binding.detailStatus.text = args.estateArgs!!.status
      binding.detailEntryDate.text = args.estateArgs!!.entryDate
      binding.detailSaleDate.text = args.estateArgs!!.saleDate
      binding.detailVicinity.text = args.estateArgs!!.vicinity.toString()
      binding.detailAddress.text = args.estateArgs!!.address
  }
    }