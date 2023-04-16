package com.example.realestatemanager.fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.realestatemanager.viewModel.EstateViewModel
import com.example.realestatemanager.R
import com.example.realestatemanager.adapters.SearchAdapter
import com.example.realestatemanager.model.EstateData
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class SearchFragment : Fragment(R.layout.fragment_search){
    private lateinit var searchType:AutoCompleteTextView
    private lateinit var searchNoResult:TextView
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
    private lateinit var estateViewModel: EstateViewModel
    private lateinit var searchResults:TextView
    private lateinit var recyclerView: RecyclerView
    private val adapter = SearchAdapter()
    private var allFieldsChecked = false
    // Date format for date picker
    private val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView=requireView().findViewById(R.id.search_result_list)
        initDropDownMenus()
        initDateBtn()
        initFab()

        }

    // Set up drop-down menus for estate's type x status
    private fun initDropDownMenus() {
        searchStatus=requireView().findViewById(R.id.search_status)
        searchType=requireView().findViewById(R.id.search_type)
        // Estate's type
        val typeItems = listOf("Apartment", "House", "Loft")
        val typeAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, typeItems)
        searchType.setAdapter(typeAdapter)
        // Estate's status
        val status = listOf("For Rent", "For Sale")
        val statusAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, status)
        searchStatus.setAdapter(statusAdapter)
    }

    // Set up btn to pick availability date
    private fun initDateBtn() {
        dateButton=requireView().findViewById(R.id.date_btn)
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
        searchResults=requireView().findViewById(R.id.search_results)
        searchFab=requireView().findViewById(R.id.search_fab)
        searchFab.setOnClickListener {
            // Check if some required fields are empty x prompt user to fill them in
            allFieldsChecked = checkAllFields()
            // After validation
            if (allFieldsChecked) {
                searchResults.visibility = View.VISIBLE
                initRecyclerView()
                initViewModel()
            }
        }
    }
    private fun initRecyclerView() {
        val recyclerView =RecyclerView(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        )
    }
    private fun initViewModel() {
        searchNoResult=requireView().findViewById(R.id.search_no_result)
        estateViewModel = ViewModelProvider(this)[EstateViewModel::class.java]
        estateViewModel.readAllData.observe(viewLifecycleOwner) { estateList ->

            val filters = filterEstates(estateList, generateFilters())

            if (filters.isNotEmpty()) {
                adapter.differ.submitList(filters)
                recyclerView.visibility = View.VISIBLE
                searchNoResult.visibility = View.GONE
                resetFilters()
            } else {
                recyclerView.visibility = View.GONE
                searchNoResult.visibility = View.VISIBLE
            }
        }
    }

    private fun generateFilters() = listOf<(EstateData) -> Boolean>(
        { it.type == searchType.text.toString() },
        { it.status == searchStatus.text.toString() },
        {
            it.surface >= Integer.parseInt(surfaceMin.text.toString()) &&
                    it.surface <= Integer.parseInt(surfaceMax.text.toString())
        },
        {
            it.rooms >= Integer.parseInt(roomsMin.text.toString()) &&
                    it.rooms <= Integer.parseInt(roomsMax.text.toString())
        },
        {
            it.price >= Integer.parseInt(priceMin.text.toString()) &&
                    it.price <= Integer.parseInt(priceMax.text.toString())
        },
        {
            outputDateFormat.parse(it.entryDate)!! <=
                    outputDateFormat.parse(selectedDate.text.toString())
        }
    )

    private fun filterEstates(estates: List<EstateData>, filters: List<(EstateData) -> Boolean>) =
        estates.filter { estate -> filters.all { filter -> filter(estate) } }

    private fun resetFilters() {
        searchType.setText("")
        searchStatus.setText("")
        surfaceMin.setText("")
        surfaceMax.setText("")
        roomsMin.setText("")
        roomsMax.setText("")
        priceMin.setText("")
        priceMax.setText("")
        selectedDate.setText(R.string.no_date_selected)
    }

    private fun checkAllFields(): Boolean {
            surfaceMin=requireView().findViewById(R.id.surface_min)
            surfaceMax=requireView().findViewById(R.id.surface_max)
            roomsMin=requireView().findViewById(R.id.rooms_min)
            roomsMax=requireView().findViewById(R.id.rooms_max)
            priceMin=requireView().findViewById(R.id.price_min)
            priceMax=requireView().findViewById(R.id.price_max)

            // Type
            if (searchType.text.toString().isEmpty()) {
                searchType.error = getString(R.string.error_type)
                return false
            } else {
                searchType.error = null
            }
            // Status
            if (searchStatus.text.toString().isEmpty()) {
                searchStatus.error = getString(R.string.error_status_search)
                return false
            } else {
                searchStatus.error = null
            }
            // Surface
            if (surfaceMin.text.toString().isEmpty()) {
                surfaceMin.error = getString(R.string.error_surface_min)
                return false
            } else {
                surfaceMin.error = null
            }
            if (surfaceMax.text.toString().isEmpty()) {
                surfaceMax.error = getString(R.string.error_surface_max)
                return false
            } else {
                surfaceMax.error = null
            }
            // No. of rooms
            if (roomsMin.text.toString().isEmpty()) {
                roomsMin.error = getString(R.string.error_rooms_min)
                return false
            } else {
                roomsMin.error = null
            }
            if (roomsMax.text.toString().isEmpty()) {
                roomsMax.error = getString(R.string.error_rooms_max)
                return false
            } else {
                roomsMax.error = null
            }
            // Price
            if (priceMin.text.toString().isEmpty()) {
                priceMin.error = getString(R.string.error_price_min)
                return false
            } else {
                priceMin.error = null
            }
            if (priceMax.text.toString().isEmpty()) {
                priceMax.error = getString(R.string.error_price_max)
                return false
            } else {
                priceMax.error = null
            }
            // Date
            if (selectedDate.text.toString() == getString(R.string.no_date_selected)) {
                Toast.makeText(
                    requireContext(),
                    R.string.error_availability_date,
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }

        return true
    }
}