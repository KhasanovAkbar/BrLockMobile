package mobile.uz.brlock_mobile.ui

import mobile.uz.brlock_mobile.R
import mobile.uz.brlock_mobile.base.BaseFragment
import mobile.uz.brlock_mobile.base.BaseViewModel
import mobile.uz.brlock_mobile.ui.activity.MainActivity
import mobile.uz.brlock_mobile.ui.auth.LogInFragment
import mobile.uz.brlock_mobile.utils.preferences.SharedManager
import org.koin.android.ext.android.inject
import java.util.*

class MainFragment : BaseFragment(R.layout.fragment_main) {
    val sharedManager: SharedManager by inject()
    override lateinit var mainActivity: MainActivity
    override lateinit var viewModel: BaseViewModel
    override fun initialize() {

        startFragment()
    }

    private fun startFragment() {
        if (sharedManager.token.isNotEmpty()) {
            replaceFragment(SearchFragment())
        } else {
            replaceFragment(LogInFragment())
        }
    }
}