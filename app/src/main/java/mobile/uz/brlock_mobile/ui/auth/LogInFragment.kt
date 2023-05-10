package mobile.uz.brlock_mobile.ui.auth

import kotlinx.android.synthetic.main.fragment_log_in.*
import mobile.uz.brlock_mobile.R
import mobile.uz.brlock_mobile.base.BaseFragment
import mobile.uz.brlock_mobile.base.BaseViewModel
import mobile.uz.brlock_mobile.domain.login.LoginResponse
import mobile.uz.brlock_mobile.ui.SearchFragment
import mobile.uz.brlock_mobile.ui.activity.MainActivity
import mobile.uz.brlock_mobile.utils.extentions.toast


class LogInFragment : BaseFragment(R.layout.fragment_log_in) {
    override lateinit var mainActivity: MainActivity
    override lateinit var viewModel: BaseViewModel

    override fun initialize() {
        mainActivity = (requireActivity() as MainActivity)
        viewModel = mainActivity.viewModel

        login_btn.setOnClickListener {
            loginUser()
        }

        sign_in.setOnClickListener {
            showProgress(true)
            addFragment(RegisterFragment())
        }
    }

    private fun loginUser() {
        val phoneNumber: String = phone_number_et.text.toString().trim()
        val password: String = password_et.text.toString().trim()

        if (phoneNumber.isEmpty()) {
            phone_number_et.error = "Username is required"
            phone_number_et.requestFocus()
            return
        } else if (password.isEmpty()) {
            password_et.error = "Password is required"
            password_et.requestFocus()
            return
        }
        showProgress(true)
        viewModel.login(phoneNumber, password)
    }

    override fun observe() {
        viewModel.single.observe(
            viewLifecycleOwner,
            {
                showProgress(false)
                if (it is LoginResponse) {
                    if (!it.requestFailed) {
                        replaceFragment(SearchFragment())

                    } else {
                        toast(requireContext(), "Failed")
                    }
                }
            }
        )
    }
}
