@file:Suppress("DEPRECATION")

package com.example.realestatemanager.viewModel
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
/**
 * Created by Philippe on 21/02/2018.
 */
object Utils {
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     */
    fun convertDollarToEuro(dollars: Int): Int {
        return Math.round(dollars * 0.812).toInt()
    }

    // Add method to convert Euro to Dollar
    fun convertEuroToDollar(euros: Int): Int {
        return Math.round(euros / 0.812).toInt()
    }// Change date format

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     */
    val todayDate: String
        get() {
            @SuppressLint("SimpleDateFormat") val dateFormat:  // Change date format
                    DateFormat = SimpleDateFormat("dd/MM/yyyy")
            return dateFormat.format(Date())
        }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     */
    @SuppressLint("MissingPermission")
    fun isInternetAvailable(context: Context): Boolean {
        // Check connectivity instead of Wifi only
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }
}