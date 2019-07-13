package pl.lukaszmarczak.assistedinjectandroidpermissionsetc.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Base ViewModel class with default Failure handling.
 * @see ViewModel
 * @see Failure
 */
abstract class BaseViewModel : ViewModel() {

    val failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
}