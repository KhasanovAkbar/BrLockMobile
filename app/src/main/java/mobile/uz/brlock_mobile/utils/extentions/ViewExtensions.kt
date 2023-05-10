package mobile.uz.brlock_mobile.utils.extentions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun View.showGone(show: Boolean): View {
    visibility = if (show) View.VISIBLE else View.GONE
    return this
}
internal fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

