package com.example.realestatemanager.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.api.AddressRepository
import com.example.realestatemanager.model.GeoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
class AddressesViewModel(private val repository:AddressRepository):ViewModel(){
    private val _addressesResponse: MutableLiveData<Response<GeoData>> = MutableLiveData()
    val addressesResponse:LiveData<Response<GeoData>>
        get() = _addressesResponse
    fun getAddressesDetails(address: String?, key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response: Response<GeoData> = repository.getAddresses(address,key)
            _addressesResponse.postValue(response)
        }
    }
}