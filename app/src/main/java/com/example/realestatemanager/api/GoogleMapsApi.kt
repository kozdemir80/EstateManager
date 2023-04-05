package com.example.realestatemanager.api
import com.example.realestatemanager.model.GeoData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
/*
 * Google maps api terms to make the request from google maps service
 */
interface GoogleMapsApi {
    @GET("geocode/json")
    suspend fun addresses(
        @Query("address")  address: String?,
        @Query("key") key: String?,
    ): Response<GeoData>
}