package pl.lukaszmarczak.assistedinjectandroidpermissionsetc.base

import androidx.appcompat.app.AppCompatActivity
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.App
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.di.ApplicationComponent

abstract class BaseActivity : AppCompatActivity() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }
}