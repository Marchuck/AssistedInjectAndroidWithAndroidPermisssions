package pl.lukaszmarczak.assistedinjectandroidpermissionsetc

import android.app.Application
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.di.ApplicationComponent
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.di.ApplicationModule
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.di.DaggerApplicationComponent

class App : Application() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}