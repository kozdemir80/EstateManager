package com.example.realestatemanager.activities

import android.annotation.SuppressLint
import android.net.Uri


import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanager.viewModel.EstateViewModel
import com.example.realestatemanager.R
import com.example.realestatemanager.adapters.GridAdapter
import com.example.realestatemanager.databinding.EditDetailsActivityBinding
import com.example.realestatemanager.model.EstateData
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult
import java.text.SimpleDateFormat
import java.util.*
class EditDetailsActivity:AppCompatActivity() {
    private lateinit var binding: EditDetailsActivityBinding
    private lateinit var estateViewModel: EstateViewModel
    private var vicinity = ArrayList<String>()
    private val photoUris = ArrayList<Uri>()
    private var photoCaptions = ArrayList<String>()
    private lateinit var adapter: GridAdapter
    private lateinit var imagePicker: ImagePicker
    private var allFieldsChecked = false
    // Date format for date picker
    private val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_details_activity)
        binding = EditDetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imagePicker = ImagePicker(this)

        estateViewModel = ViewModelProvider(this)[EstateViewModel::class.java]
        descriptionDetails()
        initDropDownMenus()
        initChipGroup()
        initDateBtn()
        initPhotoHandling()
        addEstate()
    }
    // Set up chip group to select POIs
    private fun initChipGroup() {
        binding.addVicinityBtn1.setOnClickListener {
            if (binding.addVicinity1.toString().isNotEmpty()) {
                addChip(binding.addVicinity1.text.toString())
                binding.addVicinity1.setText("")
            }
        }
    }
    private fun addChip(text: String) {
        val chip = Chip(this)
        chip.text = text
        chip.isCloseIconVisible = true
        binding.addChipGroup1.addView(chip)
        vicinity.add(text)
        chip.setOnCloseIconClickListener {
            binding.addChipGroup1.removeView(chip)
            vicinity.remove(text)
        }
    }
    private fun descriptionDetails() {
      binding.apply {
          addType1.setText(intent.getStringExtra("type"))
          addPrice1.setText(intent.getStringExtra("price")).toString()
          addSurface1.setText(intent.getStringExtra("surface")).toString()
          addRooms1.setText(intent.getStringExtra("rooms")).toString()
          addBedrooms1.setText(intent.getStringExtra("bedrooms")).toString()
          addBathrooms1.setText(intent.getStringExtra("bathrooms")).toString()
          addDescription1.setText(intent.getStringExtra("description"))
          addDistrict1.setText(intent.getStringExtra("district"))
          addAddress1.setText(intent.getStringExtra("address"))
          addVicinity1.setText(intent.getStringExtra("vicinity")).toString()
          addStatus1.setText(intent.getStringExtra("status"))
          addRealtor1.setText(intent.getStringExtra("realtor"))
          addEntryDate1.text = intent.getStringExtra("entryDate")
          addSaleDate1.text = intent.getStringExtra("saleDate")
      }}
    // Set up drop-down menus for estate's type x status
    private fun initDropDownMenus() {
        val typeItems = listOf("Apartment", "House", "Loft")
        val adapter = ArrayAdapter(applicationContext, R.layout.dropdown_item, typeItems)
        binding.addType1.setAdapter(adapter)
        val statusItems = listOf("For Rent", "For Sale")
        val adapter2 = ArrayAdapter(applicationContext, R.layout.dropdown_item, statusItems)
        binding.addStatus1.setAdapter(adapter2)
    }
    // Set up btn to pick market entry x sale date
    private fun initDateBtn() {
        // Estate's date of market entry btn
        binding.addEntryDate1.setOnClickListener {
            val entryPicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date_picker_entry)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            entryPicker.addOnPositiveButtonClickListener {
                val entrySelectedDate = outputDateFormat.format(it)
                binding.addSelectedEntryDate1.text = entrySelectedDate
            }
            entryPicker.show(supportFragmentManager, "Entry Date Picker")
        }
        // Estate's date of sale btn
        binding.addSaleDate1.setOnClickListener {
            val salePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date_picker_sale)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            salePicker.addOnPositiveButtonClickListener {
                val saleSelectedDate = outputDateFormat.format(it)
                binding.addSelectedSaleDate1.text = saleSelectedDate
            }
            salePicker.show(supportFragmentManager, "Sale Date Picker")
        }
    }
    // Set up btn to pick photo in gallery or take picture w/ camera
    private fun initPhotoHandling() {
        // Camera btn
        val recyclerView = binding.addPhotoList1
        adapter = GridAdapter(photoUris, photoCaptions, this)
        recyclerView.adapter = adapter
        binding.addPhotoCamera1.setOnClickListener {
            imagePicker.takeFromCamera { imageResult ->
                imageCallback(imageResult)
            }
        }
        // Gallery btn
        binding.addPhotoGallery1.setOnClickListener {
            imagePicker.pickFromStorage { imageResult ->
                imageCallback(imageResult)
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun imageCallback(imageResult: ImageResult<Uri>) {
        when (imageResult) {
            is ImageResult.Success -> {
                val builder = AlertDialog.Builder(this)
                val editText = EditText(this)
                builder.setTitle(R.string.alert_dialog_title)
                    .setView(editText)
                    .setPositiveButton(R.string.toolbar_add) { _, _ ->
                        // Add photo w/ caption
                        val uri = imageResult.value
                        photoUris.add(uri)
                        photoCaptions.add(editText.text.toString())
                        adapter.notifyDataSetChanged()
                    }
                    .show()
            }
            is ImageResult.Failure -> {
                val errorString = imageResult.errorString
                Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Set up fab to create new item w/ info
    private fun addEstate() {
        binding.addButton1.setOnClickListener {
            // Check if some required fields are empty x prompt user to fill them in
            allFieldsChecked = checkAllFields()
            // After validation
            if (allFieldsChecked) {
                // Convert uris to strings
                val uris = ArrayList<String>()
                for (uri in photoUris) {
                    val strUri = uri.toString()
                    uris.add(strUri)
                }
                // Create Estate object
                val estate = EstateData(
                   0,
                    type = binding.addType1.text.toString(),
                    district = binding.addDistrict1.text.toString(),
                    price = Integer.parseInt(binding.addPrice1.text.toString()),
                    description = binding.addDescription1.text.toString(),
                    surface = Integer.parseInt("0" + binding.addSurface1.text.toString()),
                    realtor = binding.addRealtor1.text.toString(),
                    status = binding.addStatus1.text.toString(),
                    rooms = Integer.parseInt("0" + binding.addRooms1.text.toString()),
                    bedrooms = Integer.parseInt("0" + binding.addBedrooms1.text.toString()),
                    bathrooms = Integer.parseInt("0" + binding.addBathrooms1.text.toString()),
                    address = binding.addAddress1.text.toString(),
                    entryDate = binding.addSelectedEntryDate1.text.toString(),
                    saleDate = binding.addSelectedSaleDate1.text.toString(),
                    vicinity,
                    uris,
                    photoCaptions
                )
                // Add data to db
                estateViewModel.updateEstate(estate)
                Toast.makeText(applicationContext, "Successfully Added", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkAllFields(): Boolean {
        binding.apply {
            // Type
            if (addType1.text.toString().isEmpty()) {
                addType1.error = getString(R.string.error_type)
                return false
            } else {
               addType1.error = null
            }
            // Price
            if (addPrice1.text.toString().isEmpty()) {
                addPrice1.error = getString(R.string.error_price)
                return false
            } else {
                addPrice1.error = null
            }
            // Address
            if (addAddress1.text.toString().isEmpty()) {
                addAddress1.error = getString(R.string.error_address)
                return false
            } else {
                addAddress1.error = null
            }
            // District
            if (addDistrict1.text.toString().isEmpty()) {
                addDistrict1.error = getString(R.string.error_district)
                return false
            } else {
                addDistrict1.error = null
            }
            // Status
            if (addStatus1.text.toString() == resources.getStringArray(R.array.status).toString()
                && addSelectedSaleDate1.text == getString(R.string.not_sold)
            ) {
                Toast.makeText(applicationContext, R.string.error_status, Toast.LENGTH_SHORT).show()
                return false
            }
            // Dates
            if (addSelectedEntryDate1.text.toString() == getString(R.string.no_date_selected)) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_entry_date),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            if (addSelectedSaleDate1.text.toString() != getString(R.string.not_sold) && (addStatus1.text.toString()
                    .isEmpty() || addStatus1.text.toString() == resources.getStringArray(R.array.status)[0])
            ) {
                Toast.makeText(applicationContext, R.string.error_sale_date, Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            // Photo
            if (photoUris.isEmpty()) {
                Toast.makeText(applicationContext, R.string.error_photo, Toast.LENGTH_SHORT)
                    .show()
                return false
            }
        }
        return true
    }
}