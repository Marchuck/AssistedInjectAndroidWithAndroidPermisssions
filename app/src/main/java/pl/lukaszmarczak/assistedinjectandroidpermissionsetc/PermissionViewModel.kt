package pl.lukaszmarczak.assistedinjectandroidpermissionsetc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.async
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.base.BaseViewModel
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.base.Either
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.base.Failure
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.base.LiveEvent
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.interactor.location.GetLastLocationUseCase
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.interactor.location.LocationFetchException
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.interactor.location.LocationResponse
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.interactor.location.NullableLocationException
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.interactor.permission.PermissionEnsurer
import kotlin.coroutines.CoroutineContext

class PermissionViewModel @AssistedInject constructor(
        private val getLastLocationUseCase: GetLastLocationUseCase,
        private val coroutineContext: CoroutineContext,
        @Assisted private val permissionEnsurer: PermissionEnsurer
) : BaseViewModel() {

    init {
        println("creating new instance of ${this.javaClass.simpleName}")
        println("hashCode ${this.hashCode()}")
    }

    data class State(val name: String,
                     val surname: String)

    fun restoreState(state: State): PermissionViewModel {
        name.value = state.name
        surname.value = state.surname
        println("state restored")
        return this
    }


    @AssistedInject.Factory
    interface Factory {
        fun create(permissionEnsurer: PermissionEnsurer): PermissionViewModel
    }

    val userLocation = MutableLiveData<LocationResponse>()

    val name = MutableLiveData<String>()

    val surname = MutableLiveData<String>()

    val navigateHome = LiveEvent<Any>()

    val checkYourLocationSettings = LiveEvent<Any>()

    fun laterClick() {
        navigateHome.value = Any()
    }

    fun findYourLocationClick() {
        viewModelScope.async(coroutineContext) {
            fetchLocation().either(handleFailure(), handleLocation())
        }
    }

    private fun handleFailure(): (Failure) -> Any {
        return {
            if (isNullableLocationError(it)) {
                checkYourLocationSettings.value = Any()
            } else {
                super.handleFailure(it)
            }
        }
    }

    private fun isNullableLocationError(it: Failure): Boolean {
        return it is NullableLocationException
    }

    private fun handleLocation(): (LocationResponse) -> Any {
        return { locationResponse ->
            userLocation.setValue(locationResponse)
        }
    }

    private suspend fun fetchLocation(): Either<Failure, LocationResponse> {
        val permissionGranted = permissionEnsurer.requestLocationPermission()
        return if (permissionGranted) {
            getLastLocationUseCase.run()
        } else {
            Either.Left(LocationFetchException(SecurityException("we need your location")))
        }
    }
}