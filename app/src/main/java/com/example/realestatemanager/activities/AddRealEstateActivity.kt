package com.example.realestatemanager.activities
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.EstateViewModel
import com.example.realestatemanager.R
import com.example.realestatemanager.adapters.GridAdapter
import com.example.realestatemanager.constants.Constants
import com.example.realestatemanager.databinding.AddItemLayoutBinding
import com.example.realestatemanager.model.EstateData
import com.example.realestatemanager.model.UserData
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.ronnie.github.imagepicker.ImageResult
import java.text.SimpleDateFormat
import java.util.*
class AddRealEstateActivity:AppCompatActivity() {
    private lateinit var binding: AddItemLayoutBinding
    private lateinit var addType :AutoCompleteTextView
    private lateinit var addStatus :AutoCompleteTextView
   private lateinit var estateViewModel: EstateViewModel
   private lateinit var adapter: GridAdapter
   private val vicinity = ArrayList<String>()
   private val photoUris = ArrayList<Uri>()
   private val photoCaptions = ArrayList<String>()
   private lateinit var auth: FirebaseAuth
   private lateinit var imagePicker:dev.ronnie.github.imagepicker.ImagePicker
   private var allFieldsChecked = false
   // Date format for date picker
   private val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).apply {
      timeZone = TimeZone.getTimeZone("UTC")
   }
   private lateinit var database: DatabaseReference
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.add_item_layout)
      binding = AddItemLayoutBinding.inflate(layoutInflater)
      setContentView(binding.root)
      database = Firebase.database.reference
      imagePicker = dev.ronnie.github.imagepicker.ImagePicker(this)
      estateViewModel = ViewModelProvider(this)[EstateViewModel::class.java]
      addType = AutoCompleteTextView(this)
      addStatus = AutoCompleteTextView(this)
      addStatus = findViewById(R.id.add_status)
      addType = findViewById(R.id.add_type)
      val typeItems = listOf("Apartment", "House", "Loft")
      val adapter = ArrayAdapter(applicationContext, R.layout.dropdown_item, typeItems)
     addType.setAdapter(adapter)
      val statusItems = listOf("For Rent", "For Sale")
      val adapter2 = ArrayAdapter(applicationContext, R.layout.dropdown_item, statusItems)
      addStatus.setAdapter(adapter2)
      dateBtn()
      initChipGroup()
      addEstate()
      inItPhotoHandling()
   }
   // Set up chip group to select POIs
   private fun initChipGroup() {
      binding.addVicinityBtn.setOnClickListener {
         if (binding.addVicinity.toString().isNotEmpty()) {
            addChip(binding.addVicinity.text.toString())
            binding.addVicinity.setText("")
         }
      }
   }
   private fun addChip(text: String) {
      val chip = Chip(applicationContext)
      chip.text = text
      chip.isCloseIconVisible = true
      binding.addChipGroup.addView(chip)
      vicinity.add(text)
      chip.setOnCloseIconClickListener {
         binding.addChipGroup.removeView(chip)
         vicinity.remove(text)
      }
   }

   private fun dateBtn(){
      binding.addEntryDate.setOnClickListener {
         val entryPicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.date_picker_entry)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
         entryPicker.addOnPositiveButtonClickListener {
            val entrySelectedDate = outputDateFormat.format(it)
            binding.addSelectedEntryDate.text = entrySelectedDate
         }
         entryPicker.show(supportFragmentManager, "Entry Date Picker")
      }
      // Estate's date of sale btn
      binding.addSaleDate.setOnClickListener {
         val salePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.date_picker_sale)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
         salePicker.addOnPositiveButtonClickListener {
            val saleSelectedDate = outputDateFormat.format(it)
            binding.addSelectedSaleDate.text = saleSelectedDate
         }
         salePicker.show(supportFragmentManager, "Sale Date Picker")
      }
   }
   // Set up btn to pick photo in gallery or take picture w/ camera
   private fun inItPhotoHandling() {
      // Init RecyclerView
      val recyclerView = binding.addPhotoList
      adapter = GridAdapter(photoUris, photoCaptions, applicationContext)
      recyclerView.adapter = adapter
      // Camera btn
      binding.addPhotoCamera.setOnClickListener {
         imagePicker.takeFromCamera { imageResult ->
            imageCallback(imageResult)
         }
      }
      // Gallery btn
      binding.addPhotoGallery.setOnClickListener {
         imagePicker.selectSource{ result ->
            imageCallback(result)
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
            Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
         }
      }
   }

   // Set up fab to create new item w/ info
   private fun addEstate() {
      binding.addButton.setOnClickListener {
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
               type = binding.addType.text.toString(),
               district = binding.addDistrict.text.toString(),
               price = Integer.parseInt(binding.addPrice.text.toString()),
               description = binding.addDescription.text.toString(),
               surface = Integer.parseInt("0" + binding.addSurface.text.toString()),
               realtor = binding.addRealtor.text.toString(),
               status = binding.addStatus.text.toString(),
               rooms = Integer.parseInt("0" + binding.addRooms.text.toString()),
               bedrooms = Integer.parseInt("0" + binding.addBedrooms.text.toString()),
               bathrooms = Integer.parseInt("0" + binding.addBathrooms.text.toString()),
               address = binding.addAddress.text.toString(),
               entryDate = binding.addSelectedEntryDate.text.toString(),
               saleDate = binding.addSelectedSaleDate.text.toString(),
               vicinity,
               uris,
               photoCaptions
            )
            // Add data to db
            estateViewModel.addEstate(estate)

            // Send notification to user
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               sendNotification()
            }
            Toast.makeText(applicationContext,"Successfully Added",Toast.LENGTH_SHORT).show()
         }
      }
   }
   private fun checkAllFields(): Boolean {
      binding.apply {
         // Type
         if (addType.text.toString().isEmpty()) {
            addType.error = getString(R.string.error_type)
            return false
         } else {
            addType.error = null
         }
         // Price
         if (addPrice.text.toString().isEmpty()) {
            addPrice.error = getString(R.string.error_price)
            return false
         } else {
            addPrice.error = null
         }
         // Address
         if (addAddress.text.toString().isEmpty()) {
            addAddress.error = getString(R.string.error_address)
            return false
         } else {
            addAddress.error = null
         }
         // District
         if (addDistrict.text.toString().isEmpty()) {
            addDistrict.error = getString(R.string.error_district)
            return false
         } else {
            addDistrict.error = null
         }
         // Status
         if (addStatus.text.toString() == resources.getStringArray(R.array.status).toString()
            && addSelectedSaleDate.text == getString(R.string.not_sold)
         ) {
            Toast.makeText(applicationContext, R.string.error_status, Toast.LENGTH_SHORT).show()
            return false
         }
         // Dates
         if (addSelectedEntryDate.text.toString() == getString(R.string.no_date_selected)) {
            Toast.makeText(
               applicationContext,
               getString(R.string.error_entry_date),
               Toast.LENGTH_SHORT
            ).show()
            return false
         }
         if (addSelectedSaleDate.text.toString() != getString(R.string.not_sold) && (addStatus.text.toString()
               .isEmpty() || addStatus.text.toString() == resources.getStringArray(R.array.status)[0])
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

   @SuppressLint("MissingPermission")
   @RequiresApi(Build.VERSION_CODES.M)
   private fun sendNotification() {
      val intent = Intent(applicationContext, MainActivity::class.java).apply {
         flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      }
      val pendingIntent: PendingIntent = PendingIntent.getActivity(
         applicationContext,
         0,
         intent,
         PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
      )

      val builder = NotificationCompat.Builder(applicationContext, Constants.Constants.CHANNEL_ID)
         .setSmallIcon(R.drawable.ic_launcher_foreground)
         .setContentTitle(getString(R.string.notification_title))
         .setContentText(getString(R.string.notification_description))
         .setStyle(
            NotificationCompat.BigTextStyle()
               .bigText(getString(R.string.notification_description_long))
         )
         .setContentIntent(pendingIntent)
         .setAutoCancel(true)
         .setPriority(NotificationCompat.PRIORITY_DEFAULT)

      with(NotificationManagerCompat.from(applicationContext)) {
         notify(Constants.Constants.NOTIFICATION_ID, builder.build())
      }
   }
}