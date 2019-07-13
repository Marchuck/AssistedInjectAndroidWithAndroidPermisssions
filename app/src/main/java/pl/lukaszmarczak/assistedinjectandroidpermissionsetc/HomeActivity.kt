package pl.lukaszmarczak.assistedinjectandroidpermissionsetc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.interactor.location.LocationResponse

class HomeActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context, location: LocationResponse? = null): Intent {
            val homeIntent = Intent(context, HomeActivity::class.java)
            homeIntent.putExtra(LocationResponse.TAG, location)
            return homeIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val locationOrNull = intent?.getSerializableExtra(LocationResponse.TAG) as? LocationResponse

        if (locationOrNull != null) {
            textview.text = locationOrNull.toString()
        } else {
            textview.text = "Location missing"
        }
    }
}
