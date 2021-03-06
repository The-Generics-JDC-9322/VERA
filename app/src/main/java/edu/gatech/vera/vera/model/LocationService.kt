package edu.gatech.vera.vera.model

import android.location.Location

/**
 * A provider of the most accurate current location. This class provides the
 * coordinates of the accurate location through ```getCoordinates()```
 */
object LocationService {

    /** The location */
    private var location: Location? = null

    /**
     * This function adds a location to the LocationService's queue and
     * calculates the current location. You might expect the most recent
     * ```Location``` object that has been passed to be the most accurate.
     * However, the last location is not always the best because the location's
     * accuracy varies. To calculate the most accurate location, we do the
     * following:
     * * Check whether the location retrieved is significantly newer than the
     * previously fetched location.
     * * Check whether the accuracy claimed by the location is better or worse
     * than that of the previous estimate.
     * * Check the provider that's associated with the new location. A hybrid
     * provider is usually the best location option.
     *
     * @param location the location to consider as a new location
     */
    fun processLocation(location: Location) {

        if (this.location == null) {
            this.location = location
            return
        }

        val oneMinInMs = 60000

        val thisTime = this.location!!.time

        // this new location is at least a minute newer than the old location
        if (location.time - thisTime < oneMinInMs) {
            this.location = location
        }

        //the new location is twice as accurate
        if (this.location!!.hasAccuracy() &&
            location.accuracy * 2 < this.location!!.accuracy ) {
            this.location = location
        }

        // TODO the last bullet point
        //if (this.location!!.provider.equals(""))

    }


    /**
     * This function returns the coordinates of the current location or
     * ```[-Inf, -Inf]``` if the location has not been set yet.
     *
     * @return the coordinates in format [lon, lat]
     */
    fun getCoordinates(): DoubleArray {
        var arr = doubleArrayOf(
            Double.NEGATIVE_INFINITY,
            Double.NEGATIVE_INFINITY
        )

        if (location != null) {
            arr = doubleArrayOf(location!!.longitude, location!!.latitude)
        }

        return arr
    }
}