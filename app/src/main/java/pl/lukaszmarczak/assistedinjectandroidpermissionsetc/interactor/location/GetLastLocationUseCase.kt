package pl.lukaszmarczak.assistedinjectandroidpermissionsetc.interactor.location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.base.Either
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.base.Failure
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.base.NoParamsUseCase
import javax.inject.Inject
import kotlin.coroutines.resume

typealias Result = Either<Failure, LocationResponse>
typealias Continuation = CancellableContinuation<Result>

data class LocationFetchException(val exception: Exception) : Failure.FeatureFailure() {
    override fun toString(): String {
        return "LocationFetchException: ${exception}"
    }
}

object NullableLocationException : Failure.FeatureFailure()

class GetLastLocationUseCase @Inject constructor(private val client: FusedLocationProviderClient) :
    NoParamsUseCase<LocationResponse>() {

    @RequiresPermission(ACCESS_FINE_LOCATION)
    override suspend fun run(): Result {
        val result: Result = createLocationCoroutine()
        return result
    }

    private suspend fun createLocationCoroutine(): Result = suspendCancellableCoroutine { continuation ->
        client.lastLocation
            .addOnSuccessListener { location: Location? ->
                handleUserLocation(location, continuation)
            }.addOnFailureListener {
                continuation.resume(Either.Left(LocationFetchException(it)))
            }
    }

    private fun handleUserLocation(
        location: Location?,
        continuation: Continuation
    ) {
        if (location != null) {
            val result = LocationResponse(
                location.latitude,
                location.longitude
            )
            continuation.resume(Either.Right(result))
        } else {
            continuation.resume(
                Either.Left(NullableLocationException)
            )
        }
    }
}