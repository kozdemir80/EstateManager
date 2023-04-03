package com.example.realestatemanager.model
import android.content.ContentValues
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.realestatemanager.constants.Constants
import com.example.realestatemanager.constants.Constants.Constants.TYPE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
@Entity(tableName = "estate_table")
@Parcelize
data class EstateData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var type: String,
    var district: String,
    var price: Int,
    var description: String,
    var surface: Int,
    var realtor: String,
    var status: String,
    var rooms: Int,
    var bedrooms: Int,
    var bathrooms: Int,
    var address: String,
    var entryDate: String,
    var saleDate: String,
    var vicinity: ArrayList<String>,
    var photoUris: ArrayList<String>,
    var photoCaptions: ArrayList<String>
):Parcelable{

    class MyVicinityConvertor {
        @TypeConverter
        fun fromString(value: String?): ArrayList<String> {
            val vicinityType = object : TypeToken<ArrayList<String?>>() {}.type
            return Gson().fromJson(value, vicinityType)
        }

        @TypeConverter
        fun fromVicinityList(vicinityList: ArrayList<String?>): String {
            return Gson().toJson(vicinityList)
        }
    }
    constructor() : this(
        0,
        "",
        "",
        0,
        "",
        0,
        "",
        "",
        0,
        0,
        0,
        "",
        "",
        "",
        arrayListOf(""),
        arrayListOf(""),
        arrayListOf("")
    )
    // --- FOR CONTENT PROVIDER ---
    companion object {
        fun fromContentValues(
            values: ContentValues
        ): EstateData {
            val estate =EstateData()
            if (values.containsKey(TYPE)) estate.type =
                values.getAsString(TYPE)
            if (values.containsKey(Constants.Constants.DISTRICT)) estate.district =
                values.getAsString(Constants.Constants.DISTRICT)
            if (values.containsKey(Constants.Constants.PRICE)) estate.price =
                values.getAsInteger(Constants.Constants.PRICE)
            if (values.containsKey(Constants.Constants.DESCRIPTION)) estate.description =
                values.getAsString(Constants.Constants.DESCRIPTION)
            if (values.containsKey(Constants.Constants.SURFACE)) estate.surface =
                values.getAsInteger(Constants.Constants.SURFACE)
            if (values.containsKey(Constants.Constants.REALTOR)) estate.realtor =
                values.getAsString(Constants.Constants.REALTOR)
            if (values.containsKey(Constants.Constants.STATUS)) estate.status =
                values.getAsString(Constants.Constants.STATUS)
            if (values.containsKey(Constants.Constants.NB_ROOMS)) estate.rooms =
                values.getAsInteger(Constants.Constants.NB_ROOMS)
            if (values.containsKey(Constants.Constants.NB_BEDROOMS)) estate.bedrooms =
                values.getAsInteger(Constants.Constants.NB_BEDROOMS)
            if (values.containsKey(Constants.Constants.NB_BATHROOMS)) estate.bathrooms =
                values.getAsInteger(Constants.Constants.NB_BATHROOMS)
            if (values.containsKey(Constants.Constants.ADDRESS)) estate.address =
                values.getAsString(Constants.Constants.ADDRESS)
            if (values.containsKey(Constants.Constants.ENTRY_DATE)) estate.entryDate =
                values.getAsString(Constants.Constants.ENTRY_DATE)
            if (values.containsKey(Constants.Constants.SALE_DATE)) estate.saleDate =
                values.getAsString(Constants.Constants.SALE_DATE)
            if (values.containsKey(Constants.Constants.VICINITY)) estate.vicinity =
                arrayListOf(values.getAsString(Constants.Constants.VICINITY))
            if (values.containsKey(Constants.Constants.PHOTO_URIS)) estate.photoUris =
                arrayListOf(values.getAsString(Constants.Constants.PHOTO_URIS))
            if (values.containsKey(Constants.Constants.PHOTO_CAPTIONS)) estate.photoCaptions =
                arrayListOf(values.getAsString(Constants.Constants.PHOTO_CAPTIONS))
            return estate
        }
    }

}
