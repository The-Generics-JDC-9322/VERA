package edu.gatech.vera.vera.model

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

object LocationService {

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    fun onCreate(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }
}