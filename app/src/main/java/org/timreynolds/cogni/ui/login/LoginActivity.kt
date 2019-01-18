package org.timreynolds.cogni.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_login.*
import org.timreynolds.cogni.R
import org.timreynolds.cogni.MyApplication
import org.timreynolds.cogni.ui.base.BaseActivity
import org.timreynolds.cogni.ui.main.MainActivity
import org.timreynolds.cogni.utils.Logger
import org.timreynolds.cogni.utils.hideKeyboard


class LoginActivity : BaseActivity(), LoginMvpView {

    private lateinit var loginPresenter: LoginPresenter<LoginMvpView>

    companion object {
        private val TAG: String = LoginActivity::class.java.simpleName
        fun getNewIntent(context: Context) = Intent(context,LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginPresenter = LoginPresenter((application as MyApplication).dataManager)

        loginPresenter.onAttach(this)
        loginPresenter.isLoggedIn()

        login.setOnClickListener {
            email.hideKeyboard()
            // loginPresenter.doLogin(username.text.toString(), email.text.toString())
            Logger.d(TAG, "submit -> " + username + " : " + email)
            loginPresenter.doSignUp(username.text.toString(), email.text.toString())
        }

        email.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == R.id.otp || id == EditorInfo.IME_ACTION_DONE) {
                email.hideKeyboard()
                // loginPresenter.doLogin(username.text.toString(), email.text.toString())
                loginPresenter.doSignUp(username.text.toString(), email.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun showError(message: String) {
        onError(message)
    }

    override fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
