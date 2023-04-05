package com.example.realestatemanager.activities
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.realestatemanager.R
import com.example.realestatemanager.adapters.PhotoAdapter
import com.example.realestatemanager.databinding.DescriptionActivityBinding
import com.example.realestatemanager.model.EstateData
import com.google.gson.Gson
class DescriptionDetailsFromMapActivity:AppCompatActivity() {
    private lateinit var binding: DescriptionActivityBinding
    private val photoUris = ArrayList<Uri>()
    private var photoCaptions = ArrayList<String>()
    private lateinit var adapter: PhotoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.description_activity)
        binding=DescriptionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

            descriptionDetailsFromMap()
            sliderFromMap()
        //Fetches details for update
        binding.detailFab.setOnClickListener {
            val myJson = intent.getStringExtra("markerTag").toString()
            val json = Gson()
            // Fetches the restaurant details from mapview marker
            val details = json.fromJson(myJson, EstateData::class.java)
            val intent = Intent(this, EditDetailsActivity::class.java)
            intent.putExtra("id",details.id).toString()
            intent.putExtra("description", details.description)
            intent.putExtra("type", details.type)
            intent.putExtra("price", details.price.toString())
            intent.putExtra("surface", details.surface.toString())
            intent.putExtra("rooms", details.rooms.toString())
            intent.putExtra("bedrooms", details.bedrooms.toString())
            intent.putExtra("bathrooms",details.bathrooms.toString())
            intent.putExtra("realtor", details.realtor)
            intent.putExtra("status", details.status)
            intent.putExtra("entryDate",details.entryDate)
            intent.putExtra("saleDate", details.saleDate)
            intent.putExtra("vicinity", details.vicinity.toString())
            intent.putExtra("address", details.address)
            intent.putExtra("district", details.district)
            startActivity(intent)
        }
}
    private fun sliderFromMap(){

        val myJson = intent.getStringExtra("markerTag").toString()
        val json = Gson()
        // Fetches the restaurant details from mapview marker
        val details = json.fromJson(myJson, EstateData::class.java)
        // Get uris
        for (strUri in details.photoUris) {
            val uri = Uri.parse(strUri)
            photoUris.add(uri)
        }
        // Get captions
        photoCaptions = details.photoCaptions
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
    private fun descriptionDetailsFromMap(){
        val myJson = intent.getStringExtra("markerTag")
        val json = Gson()
        // Fetches the restaurant details from mapview marker
        val details = json.fromJson(myJson, EstateData::class.java)
        binding.detailDescription.text = details.description
        binding.detailSurface.text = details.surface.toString()
        binding.detailRooms.text = details.rooms.toString()
        binding.detailBedrooms.text = details.bedrooms.toString()
        binding.detailBathrooms.text = details.bathrooms.toString()
        binding.detailRealtor.text = details.realtor
        binding.detailStatus.text = details.status
        binding.detailEntryDate.text = details.entryDate
        binding.detailSaleDate.text = details.saleDate
        binding.detailVicinity.text = details.vicinity.toString()
        binding.detailAddress.text =details.address}

}