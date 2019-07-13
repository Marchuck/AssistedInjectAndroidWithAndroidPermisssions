package pl.lukaszmarczak.assistedinjectandroidpermissionsetc.interactor.permission

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.github.florent37.runtimepermission.kotlin.PermissionException
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission

class AndroidPermissionEnsurer(private val activity: FragmentActivity) : PermissionEnsurer {

    override suspend fun requestLocationPermission(): Boolean {
        return try {
            activity.askPermission(ACCESS_FINE_LOCATION)
            true
        } catch (ex: PermissionException) {
            false
        }
    }

    override fun isLocationPermissionGranted(): Boolean {
        val permissionState = ContextCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
}