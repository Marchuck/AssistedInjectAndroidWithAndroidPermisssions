package pl.lukaszmarczak.assistedinjectandroidpermissionsetc.interactor.location

import java.io.Serializable

data class LocationResponse(val latitude: Double, val longitude: Double) : Serializable {
    companion object {
        const val TAG = "LocationResponse"
    }

    override fun toString(): String {
        return "latitude=$latitude, longitude=$longitude"
    }
}