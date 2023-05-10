package mobile.uz.brlock_mobile.utils.extentions

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import timber.log.Timber

private var toast: Toast? = null
fun toast(context: Context, message: String) {
    toast?.apply { cancel() }
    toast = Toast.makeText(context, message, Toast.LENGTH_SHORT).apply { show() }
}


fun logi(message: String, tag: String = "RRR") {
    Timber.tag(tag).i(message)
}

fun loge(clazz: Any, tag: String = "RRR") {
    Timber.tag(tag).e(Gson().toJson(clazz))
}


