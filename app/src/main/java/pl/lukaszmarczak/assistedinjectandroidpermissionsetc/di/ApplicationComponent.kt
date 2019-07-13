package pl.lukaszmarczak.assistedinjectandroidpermissionsetc.di

import dagger.Component
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.App
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.MainActivity
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.PermissionViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, AssistedInjectModule::class])
interface ApplicationComponent {

    fun inject(application: App)

    fun inject(mainActivity: MainActivity)

    val permissionViewModelFactory: PermissionViewModel.Factory
}