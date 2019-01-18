
package org.timreynolds.cogni.ui.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.ProgressBar
import org.timreynolds.cogni.utils.NetworkUtils
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper



/**
 * BaseActivity
 */
abstract class  BaseActivity : AppCompatActivity(), MvpView {

    // if needed implement
    private var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    override fun showMessage(message: String) {
        showSnackBar(message)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onError(error: String) {
        showSnackBar(error)
    }

    override fun onError(resId: Int) {
        showSnackBar(getString(resId))
    }

    override fun isNetworkConnected(): Boolean {
        return NetworkUtils.isNetworkConnected(applicationContext)
    }


}