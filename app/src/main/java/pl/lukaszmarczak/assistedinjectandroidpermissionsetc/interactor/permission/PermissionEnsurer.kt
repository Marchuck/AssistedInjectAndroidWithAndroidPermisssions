package pl.lukaszmarczak.assistedinjectandroidpermissionsetc.interactor.permission

interface PermissionEnsurer {

    fun isLocationPermissionGranted(): Boolean

    /**
     * invokes system android asking for permissions, returns true if permission is granted
     */
    suspend fun requestLocationPermission(): Boolean
}