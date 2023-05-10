package mobile.uz.brlock_mobile.ui.auth

import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_register.*
import mobile.uz.brlock_mobile.R
import mobile.uz.brlock_mobile.base.BaseFragment
import mobile.uz.brlock_mobile.base.BaseViewModel
import mobile.uz.brlock_mobile.domain.register.RegisterRequest
import mobile.uz.brlock_mobile.domain.register.RegisterResponse
import mobile.uz.brlock_mobile.ui.activity.MainActivity
import mobile.uz.brlock_mobile.utils.extentions.toast

class RegisterFragment : BaseFragment(R.layout.fragment_register) {
    override lateinit var mainActivity: MainActivity
    override lateinit var viewModel: BaseViewModel

    override fun initialize() {
        mainActivity = (requireActivity() as MainActivity)
        viewModel = mainActivity.viewModel
        showProgress(false)
        register_btn.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val name = name_et.text.toString().trim()
        val surname = surname_et.text.toString().trim()
        val password = password_et.text.toString().trim()
        val phoneNumber = phone_number_et.rawText.trim()
        val carPlateNumber = car_number_et.text.toString().trim()

        if (name.isEmpty()) {
            name_et.error = "Name is required"
            name_et.requestFocus()
            return
        }
        if (surname.isEmpty()) {
            surname_et.error = "Surname is required"
            surname_et.requestFocus()
            return
        }
        if (password.isEmpty()) {
            password_et.error = "Password is required"
            password_et.requestFocus()
            return
        }
        if (phoneNumber.isEmpty()) {
            phone_number_et.error = "Phone number is required"
            phone_number_et.requestFocus()
            return
        }
        if (carPlateNumber.isEmpty()) {
            car_number_et.error = "Car plate is required"
            car_number_et.requestFocus()
            return
        }
        val roles = listOf("ROLE_USER")

        viewModel.register(name, surname, "+998$phoneNumber", carPlateNumber, password, roles)
        showProgress(true)

        viewModel.single.observe(viewLifecycleOwner, {
            showProgress(true)
            if (it is RegisterResponse) {
                if (!it.requestFailed) {
                    finishFragment()
                    showProgress(false)
                } else {
                    toast(requireContext(), it.failureMessage.exceptionMessage)
                    showProgress(false)
                }
            }
        })
    }
}