package mobile.uz.brlock_mobile.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*
import mobile.uz.brlock_mobile.R
import mobile.uz.brlock_mobile.base.BaseActivity
import mobile.uz.brlock_mobile.base.BaseViewModel
import mobile.uz.brlock_mobile.base.initialFragment
import mobile.uz.brlock_mobile.ui.MainFragment
import mobile.uz.brlock_mobile.utils.extentions.showGone
import mobile.uz.brlock_mobile.utils.preferences.SharedManager
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(R.layout.activity_main) {

    val viewModel by viewModel<BaseViewModel>()
    val sharedManager: SharedManager by inject()

    override fun onActivityCreated() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
    }

    private fun init() {

        viewModel.apply {
            parentLayoutId = R.id.fragmentContainer
            navLayoutId = R.id.navContainer

//            fetchData()
        }


        debug()
//        startFragment()
    }


    private fun debug() = initialFragment(MainFragment())

//    private fun startFragment() {
//        initialFragment(
//            if (validateJwtToken()) BlankFragment()
//            else LogInFragment(), true
//        )
//    }


    fun showProgress(show: Boolean) {
        progressBar.showGone(show)
    }


}

