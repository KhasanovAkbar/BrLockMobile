package mobile.uz.brlock_mobile.di


import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import mobile.uz.brlock_mobile.base.BaseViewModel
import mobile.uz.brlock_mobile.network.ErrorResp
import mobile.uz.brlock_mobile.utils.preferences.PreferenceHelper
import mobile.uz.brlock_mobile.utils.preferences.SharedManager
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val viewModelModule = module {

    fun provideMutableLiveData() = MutableLiveData<String>()

    viewModel { BaseViewModel(get(), get(), get()) }

    single { provideMutableLiveData() }
    single(named("sharedLive")) { provideMutableLiveData() }
    single(named("errorLive")) { MutableLiveData<ErrorResp>() }
}

val networkModule = module {

    fun provideGson() = Gson()

    single { provideGson() }
}

val sharedPrefModule = module {

    factory { PreferenceHelper.customPrefs(get(), "Saved token") }

    factory { SharedManager(get(), get(), get()) }
}
