package mobile.uz.brlock_mobile.utils.preferences


import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import mobile.uz.brlock_mobile.utils.preferences.PreferenceHelper.get
import mobile.uz.brlock_mobile.utils.preferences.PreferenceHelper.set

class SharedManager(
    private val preferences: SharedPreferences,
    private val gson: Gson,
    private val context: Context
) {

    companion object {
        const val TOKEN = "Brlock"
        const val TOKEN1 = "Brlock"
    }

    var token: String
        get() = preferences.get(TOKEN, "")
        set(value) {
            preferences.set(TOKEN, value)
        }

    var userList: String
        get() = preferences.get(TOKEN1, "")
        set(value) {
            preferences.set(TOKEN1, value)
        }


    fun deleteAll() {
        preferences.edit().clear().apply()
        preferences.registerOnSharedPreferenceChangeListener(object :
            SharedPreferences.OnSharedPreferenceChangeListener {
            override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {

            }

        })
    }
}
