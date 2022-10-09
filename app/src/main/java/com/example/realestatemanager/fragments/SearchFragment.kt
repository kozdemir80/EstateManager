package com.example.realestatemanager.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import com.example.realestatemanager.R
import com.example.realestatemanager.activities.EstateListActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var searchType:AutoCompleteTextView
    private lateinit var searchStatus:AutoCompleteTextView
    private lateinit var surfaceMin: TextInputEditText
    private lateinit var surfaceMax:TextInputEditText
    private lateinit var roomsMin:TextInputEditText
    private lateinit var roomsMax:TextInputEditText
    private lateinit var priceMin:TextInputEditText
    private lateinit var priceMax:TextInputEditText
    private lateinit var dateButton:Button
    private lateinit var selectedDate:TextView
    private lateinit var searchFab:FloatingActionButton
    private var categories:ArrayList<String> = ArrayList()
    // Date format for date picker
    private val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         dropDownMenus()
         allFields()
         dateButton()
}
    private fun dropDownMenus(){
        searchType= AutoCompleteTextView(requireActivity())
        searchStatus= AutoCompleteTextView(requireActivity())
        searchStatus=requireView().findViewById(R.id.search_type)
        searchType=requireView().findViewById(R.id.search_status)
        val items = listOf("Apartment","House","Loft")
        val adapter = ArrayAdapter(requireActivity(), R.layout.dropdown_item, items)
        searchType.setAdapter(adapter)
        val items2= listOf("For Rent","For Sale")
        val adapter2= ArrayAdapter(requireActivity(), R.layout.dropdown_item,items2)
        searchStatus.setAdapter(adapter2)
    }

    private fun allFields() : Boolean {
        val preferences=activity?.getSharedPreferences("myPreferences",Context.MODE_PRIVATE)
        val editor=preferences?.edit()
        surfaceMin= TextInputEditText(requireActivity())
        surfaceMin=requireView().findViewById(R.id.surface_min)
        surfaceMax= TextInputEditText(requireActivity())
        surfaceMax=requireView().findViewById(R.id.surface_max)
        roomsMin= TextInputEditText(requireActivity())
        roomsMin=requireView().findViewById(R.id.rooms_min)
        roomsMax= TextInputEditText(requireActivity())
        roomsMax=requireView().findViewById(R.id.rooms_max)
        priceMin= TextInputEditText(requireActivity())
        priceMin=requireView().findViewById(R.id.price_min)
        priceMax= TextInputEditText(requireActivity())
        priceMax=requireView().findViewById(R.id.price_max)
        if (searchType.text.toString().isEmpty()) {
            searchType.error = getString(R.string.error_type)
            return false
        } else {
            searchType.error = null
        }
        val type=searchType.text.toString()
        editor!!.putString("type",type)

        // Status
        if (searchStatus.text.toString().isEmpty()) {
            searchStatus.error = getString(R.string.error_status_search)
            return false
        } else {
            searchStatus.error = null
        }
        val status=searchStatus.text.toString()
        editor.putString("status",status)
        // Surface
        if (surfaceMin.text.toString().isEmpty()) {
            surfaceMin.error = getString(R.string.error_surface_min)
            return false
        } else {
            surfaceMin.error = null
        }
        val surfMin=surfaceMin.text.toString()
        editor.putString("surfaceMin",surfMin)
        if (surfaceMax.text.toString().isEmpty()) {
            surfaceMax.error = getString(R.string.error_surface_max)
            return false
        } else {
            surfaceMax.error = null
        }
        val surfMax=surfaceMax.text.toString()
        editor.putString("surfaceMax",surfMax)
        // No. of rooms
        if (roomsMin.text.toString().isEmpty()) {
            roomsMin.error = getString(R.string.error_rooms_min)
            return false
        } else {
            roomsMin.error = null
        }
        val rMin=roomsMin.text.toString()
        editor.putString("roomsMin",rMin)

        if (roomsMax.text.toString().isEmpty()) {
            roomsMax.error = getString(R.string.error_rooms_max)
            return false
        } else {
            roomsMax.error = null
        }
        val rMax=roomsMax.text.toString()
        editor.putString("roomsMax",rMax)

        // Price
        if (priceMin.text.toString().isEmpty()) {
            priceMin.error = getString(R.string.error_price_min)
            return false
        } else {
            priceMin.error = null
        }
        val pMin=priceMin.text.toString()
        editor.putString("priceMin",pMin)
        if (priceMax.text.toString().isEmpty()) {
            priceMax.error = getString(R.string.error_price_max)
            return false
        } else {
            priceMax.error = null
        }
        val pMax=priceMax.text.toString()
        editor.putString("priceMax",pMax)
        // Date
        if (selectedDate.text.toString() == getString(R.string.no_date_selected)) {
            Toast.makeText(
                requireContext(),
                R.string.error_availability_date,
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        val sDate=selectedDate.text.toString()
        editor.putString("selectedDate",sDate)
            editor.apply()
       return true
    }




    private fun dateButton(){
        dateButton= Button(requireActivity())
        dateButton=requireView().findViewById(R.id.date_btn)
        selectedDate= TextView(requireActivity())
        selectedDate=requireView().findViewById(R.id.selected_date)
        dateButton.setOnClickListener {
            val entryPicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date_picker_entry)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            entryPicker.addOnPositiveButtonClickListener {
                val entrySelectedDate = outputDateFormat.format(it)
                selectedDate.text = entrySelectedDate
            }
            entryPicker.show(parentFragmentManager, "Entry Date Picker")
        }
    }
    // Set up fab to filter estates w/ filled-in info
    private fun initFab() {
        searchFab.setOnClickListener {

            // After validation
            if (allFields()) {
               val intent=Intent(requireActivity(),EstateListActivity::class.java)
                startActivity(intent)

            }
        }
    }




}