package mobile.uz.brlock_mobile.app


import android.app.Application
import androidx.databinding.ktx.BuildConfig
import mobile.uz.brlock_mobile.di.networkModule
import mobile.uz.brlock_mobile.di.sharedPrefModule
import mobile.uz.brlock_mobile.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    companion object {
        private lateinit var app: App
        fun get(): App = app
    }

    override fun onCreate() {
        super.onCreate()
        app = this

        initFirebase()

        initLogger()

        initKoin()
    }

    private fun initFirebase() {
//        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun initKoin() {

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    viewModelModule,
                    networkModule,
                    sharedPrefModule
                )
            )
        }
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}