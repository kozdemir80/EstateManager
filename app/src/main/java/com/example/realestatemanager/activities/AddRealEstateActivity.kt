package com.example.realestatemanager.activities
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.realestatemanager.R
import com.example.realestatemanager.databinding.AddItemLayoutBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class AddRealEstateActivity:AppCompatActivity() {

    private lateinit var binding:AddItemLayoutBinding
    // Date format for date picker
    private val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item_layout)
        binding = AddItemLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun dropDownItems(){
        // Estate's type
        val items = listOf("Apartment","House","Loft")
        val typeAdapter = ArrayAdapter(this, R.layout.dropdown_item, items)
        binding.addType.setAdapter(typeAdapter)
        // Estate's status
        val items2= listOf("For Rent","For Sale")
        val statusAdapter= ArrayAdapter(this, R.layout.dropdown_item,items2)
        binding.addStatus.setAdapter(statusAdapter)
    }

    // Set up btn to pick market entry x sale date
    private fun initDateBtn() {
        // Estate's date of market entry btn
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
                .setTitleText(getString(R.string.date_sale))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            salePicker.addOnPositiveButtonClickListener {
                val saleSelectedDate = outputDateFormat.format(it)
                binding.addSelectedSaleDate.text = saleSelectedDate
            }
            salePicker.show(supportFragmentManager, "Sale Date Picker")
        }
    }


}