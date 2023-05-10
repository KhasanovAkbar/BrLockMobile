package mobile.uz.brlock_mobile.base


import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.jafton.tms.base.exitVariant

abstract class BaseActivity(@LayoutRes private val layoutId: Int) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        onActivityCreated()

    }


    abstract fun onActivityCreated()

    override fun onBackPressed() {
        when {
            supportFragmentManager.backStackEntryCount > 0 -> finishFragment()
            supportFragmentManager.backStackEntryCount == 0 -> exitVariant()
            else -> super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragmentsActivityResults(requestCode, resultCode, data)
    }

    private fun fragmentsActivityResults(requestCode: Int, resultCode: Int, data: Intent?) {
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

}