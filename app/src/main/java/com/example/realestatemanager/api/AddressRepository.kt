package com.example.realestatemanager.api
import com.example.realestatemanager.model.GeoData
import retrofit2.Response
class AddressRepository {
    // --- REPOSİTORY FOR GOOGLE MAPS ---
    suspend fun getAddresses(
        address: String?,
        key: String
    ): Response<GeoData> {
        return AddressInstance.api.addresses( address, key)
    }
}