package pl.lukaszmarczak.assistedinjectandroidpermissionsetc

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.base.BaseActivity
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.base.BaseViewModelFactory
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.interactor.permission.AndroidPermissionEnsurer

class MainActivity : BaseActivity() {

    private val viewModel: PermissionViewModel by lazy {
        createWithAssistedInject()
    }

    //every activity rotation implies fresh instance of PermissionViewModel backed with PermissionEnsurer
    fun createWithAssistedInject(): PermissionViewModel {
        return appComponent.permissionViewModelFactory.create(AndroidPermissionEnsurer(this))
    }

    //BAD - //every activity rotation implies keeps the same instance of PermissionViewModel
    // this will broke PermissionEnsurer functionality
    fun createUsingViewModelProviders(): PermissionViewModel {
        val factory = appComponent.permissionViewModelFactory
        val permissionEnsurer = AndroidPermissionEnsurer(this)

        return ViewModelProviders.of(
                this,
                BaseViewModelFactory {
                    return@BaseViewModelFactory factory.create(permissionEnsurer)
                }).get(PermissionViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appComponent.inject(this)

        viewModel.restoreState(PermissionViewModel.State(
                savedInstanceState?.getString("name", null) ?: "",
                savedInstanceState?.getString("surname", null) ?: ""
        ))

        find_your_location_button.setOnClickListener {
            viewModel.findYourLocationClick()
        }
        later_button.setOnClickListener {
            viewModel.laterClick()
        }

        name.addTextChangedListener(SimpleTextWatcher {
            viewModel.name.value = it
        })
        surname.addTextChangedListener(SimpleTextWatcher { value ->
            viewModel.surname.value = value
        })

        viewModel.navigateHome.observe(this, Observer {
            startActivity(HomeActivity.createIntent(this))
        })
        viewModel.userLocation.observe(this, Observer {
            println("user location $it")
            startActivity(HomeActivity.createIntent(this, it))
        })
        viewModel.checkYourLocationSettings.observe(this, Observer {
            Toast.makeText(this, "Please make sure location is enabled", Toast.LENGTH_LONG).show()
        })

        viewModel.failure.observe(this, Observer {
            Toast.makeText(this, "Error:\n$it", Toast.LENGTH_LONG).show()
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("name", viewModel.name.value)
        outState.putString("surname", viewModel.surname.value)
        super.onSaveInstanceState(outState)
    }

    class SimpleTextWatcher(val afterTextChanged: (newValue: String) -> Unit) : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            val value = p0?.trim()?.toString() ?: ""
            afterTextChanged(value)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    }
}