package pl.lukaszmarczak.assistedinjectandroidpermissionsetc.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import pl.lukaszmarczak.assistedinjectandroidpermissionsetc.App
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
class ApplicationModule(private val application: App) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providesFusedLocationProviderClient() = FusedLocationProviderClient(application)

    @Provides
    @Singleton
    fun providesMainCoroutineContext(): CoroutineContext = Dispatchers.Main

}