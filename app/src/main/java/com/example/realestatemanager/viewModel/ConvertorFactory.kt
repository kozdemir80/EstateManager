package com.example.realestatemanager.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanager.api.AddressRepository
@Suppress("UNCHECKED_CAST")
class ConvertorFactory(private val repository:AddressRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddressesViewModel(repository) as T
    }
}