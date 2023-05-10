package mobile.uz.brlock_mobile.utils.preferences

import android.content.Context
import android.content.SharedPreferences


class YourPreference private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences? =
        context.getSharedPreferences("YourCustomNamedPreference", Context.MODE_PRIVATE)

    fun saveData(key: String?, value: String?) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.commit()
    }

    fun getData(key: String?): String? {
        return if (sharedPreferences != null) {
            sharedPreferences.getString(key, "")
        } else ""
    }

    companion object {
        private var yourPreference: YourPreference? = null
        fun getInstance(context: Context): YourPreference? {
            if (yourPreference == null) {
                yourPreference = YourPreference(context)
            }
            return yourPreference
        }
    }

}