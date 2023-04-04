package com.example.realestatemanager.fragments
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.realestatemanager.EstateViewModel
import com.example.realestatemanager.R
import com.example.realestatemanager.adapters.RealEstateAdapter
import com.example.realestatemanager.model.EstateData
//Displaying Items in a list
class EstateListFragment:Fragment(R.layout.estate_list_fragment) {
    private lateinit var realEstateAdapter:RealEstateAdapter
    private lateinit var estateViewModel:EstateViewModel
    private lateinit var estaListRecyclerView: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        realEstateAdapter=RealEstateAdapter()
        estaListRecyclerView= RecyclerView(requireActivity())
        estaListRecyclerView=view.findViewById(R.id.estate_list_recyclerView)
        estaListRecyclerView.adapter=realEstateAdapter
        estaListRecyclerView.layoutManager=LinearLayoutManager(requireActivity())
        estaListRecyclerView.setHasFixedSize(true)
        estateViewModel= ViewModelProvider(this)[EstateViewModel::class.java]
        estateViewModel.readAllData.observe(({ lifecycle })){estates->
           realEstateAdapter.setData(estates)
        realEstateAdapter.setOnItemClickListener(object : RealEstateAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val id=estates[position].id
                val type=estates[position].type
                val district=estates[position].district
                val price=estates[position].price
                val description=estates[position].description
                val surface=estates[position].surface
                val rooms=estates[position].rooms
                val bedrooms=estates[position].bedrooms
                val bathrooms=estates[position].bathrooms
                val realtor=estates[position].realtor
                val status=estates[position].status
                val entryDate=estates[position].entryDate
                val saleDate=estates[position].saleDate
                val vicinity=estates[position].vicinity
                val address=estates[position].address
                val photoUris=estates[position].photoUris
                val photoCaptions=estates[position].photoCaptions
                val estateData=EstateData(id,type,district,price,description,surface,realtor,status,
                    rooms,bedrooms,bathrooms,address,entryDate,saleDate,vicinity,photoUris,photoCaptions)
                val action=EstateListFragmentDirections.actionEstateListFragmentToDescriptionActivity(estateData)
                findNavController().navigate(action)


            }
        })
        }
    }
}