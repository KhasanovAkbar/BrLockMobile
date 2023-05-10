package mobile.uz.brlock_mobile.ui

import kotlinx.android.synthetic.main.fragment_edit_user.*
import kotlinx.android.synthetic.main.fragment_edit_user.car_number_et
import kotlinx.android.synthetic.main.fragment_edit_user.name_et
import kotlinx.android.synthetic.main.fragment_edit_user.phone_number_et
import kotlinx.android.synthetic.main.fragment_edit_user.surname_et
import kotlinx.android.synthetic.main.fragment_register.*
import mobile.uz.brlock_mobile.R
import mobile.uz.brlock_mobile.base.BaseFragment
import mobile.uz.brlock_mobile.base.BaseViewModel
import mobile.uz.brlock_mobile.domain.edit.EditResponse
import mobile.uz.brlock_mobile.ui.activity.MainActivity
import mobile.uz.brlock_mobile.ui.auth.LogInFragment

class EditUserFragment : BaseFragment(R.layout.fragment_edit_user) {

    override lateinit var mainActivity: MainActivity
    override lateinit var viewModel: BaseViewModel
    private var userName: String? = null
    private var userSurname: String? = null

    override fun initialize() {
        val userJpo = viewModel.fetchUser().userJpo
        showProgress(false)
        name_et.setText(userJpo!!.name)
        surname_et.setText(userJpo.surname)
        phone_number_et.setText(userJpo.phoneNumber.subSequence(4, userJpo.phoneNumber.length))
        car_number_et.setText(userJpo.carNumber)
        edit_btn.setOnClickListener {
            showProgress(true)
            editUser()
        }

        log_out.setOnClickListener {
            logOut()
        }
    }

    private fun logOut() {
        viewModel.logOut()
        replaceFragment(LogInFragment())
        viewModel.single.value = null
    }

    private fun editUser() {
        userName = name_et.text.toString().trim()
        userSurname = surname_et.text.toString().trim()
        val phoneNumber = phone_number_et.rawText.trim()
        val carPlateNumber = car_number_et.text.toString().trim()
        if (userName!!.isEmpty()) {
            name_et.error = "Name is required"
            name_et.requestFocus()
            return
        }
        if (userSurname!!.isEmpty()) {
            surname_et.error = "Surname is required"
            surname_et.requestFocus()
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

        viewModel.editUser(
            viewModel.fetchUser().userJpo!!.id,
            userName!!,
            userSurname!!,
            "+998$phoneNumber",
            carPlateNumber
        )
    }

    override fun observe() {
        viewModel.single.observe(viewLifecycleOwner,
            {
                if (it is EditResponse) {
                    if (!it.requestFailed) {
                        SearchFragment.newInstance(it.entities[0].name, it.entities[0].surname)
                        finishFragment()
                        showProgress(false)
                        it.requestFailed = true
                    }
                }
            })
    }



}