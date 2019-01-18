package org.timreynolds.cogni.ui.login

import android.text.TextUtils
import android.util.Log
import org.timreynolds.cogni.R
import org.timreynolds.cogni.data.DataManager
import org.timreynolds.cogni.data.LoginResponse
import org.timreynolds.cogni.ui.base.BasePresenter
import org.timreynolds.cogni.utils.Logger
import org.timreynolds.cogni.utils.isEmailValid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * LoginPresenter
 */
class LoginPresenter<V : LoginMvpView>(private val dataManager: DataManager) : BasePresenter<V>(), LoginMvpPresenter<V> {

    companion object {
        private val TAG: String = LoginPresenter::class.java.simpleName
    }

    override fun doSignUp(username: String, email: String) {
        if (TextUtils.isEmpty(username)) {
            mvpView?.onError(R.string.enter_valid_username)
            return
        }

        if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
            mvpView?.onError(R.string.enter_valid_email)
            return
        }
        Log.i(TAG, "name and email -> " + username + " : " + email)
        mvpView?.showMessage("Processing request")
        Logger.d(TAG, "Sending sign up request")

        if(mvpView!!.isNetworkConnected()) {
            dataManager.doSignUp(username, email).enqueue(object : Callback<LoginResponse?> {
                override fun onFailure(call: Call<LoginResponse?>?, t: Throwable?) {
                    Logger.e(TAG, "Sign Up failure")
                    mvpView?.onError(t?.localizedMessage!!)
                }

                override fun onResponse(call: Call<LoginResponse?>?, response: Response<LoginResponse?>?) {
                    if (response != null && response.isSuccessful) {
                        Log.i(TAG, "response -> " + response.body().toString())
                        mvpView?.openMainActivity()
                        dataManager.saveToken(response.headers().get("token")!!)
                        dataManager.saveName(response.body()!!.user.name)
                        dataManager.saveEmail(response.body()!!.user.email)
                        Log.i(TAG, "response token -> " + dataManager.getToken())
                        Log.i(TAG, "Sign Up success")
                    } else {
                        mvpView?.onError(R.string.login_error_text)
                    }
                }
            })
        } else {
            mvpView?.onError(R.string.error_no_network)
        }
    }

    override fun doLogin(username: String, email: String) {
        if (TextUtils.isEmpty(username)) {
            mvpView?.onError(R.string.enter_valid_username)
            return
        }

        if (TextUtils.isEmpty(email)) {
            mvpView?.onError(R.string.enter_valid_email)
            return
        }
        Logger.d(TAG, "name and email -> " + username + " : " + email)
        mvpView?.showMessage("Loggin In")
        Logger.d(TAG, "Sending login request")
        dataManager.doLogin(username, email).enqueue(object : Callback<LoginResponse?> {
            override fun onFailure(call: Call<LoginResponse?>?, t: Throwable?) {
                Logger.e(TAG, "Login failure")
                mvpView?.hideProgress()
                mvpView?.onError(t?.localizedMessage!!)
            }

            override fun onResponse(call: Call<LoginResponse?>?, response: Response<LoginResponse?>?) {
                mvpView?.hideProgress()
                if (response != null && response.isSuccessful) {
                    Logger.d(TAG, "response -> " + response.body().toString())
                    mvpView?.openMainActivity()
                    // dataManager.saveToken(response.headers().get("token")!!)
                    dataManager.saveName(response.body()!!.user.name)
                    dataManager.saveEmail(response.body()!!.user.email)
                    Logger.d(TAG, "Login success")
                } else {
                    if(response?.errorBody() != null) {
                        Logger.e(TAG, "Login not success -> " + response.errorBody().toString())
                        //val gson = Gson()
                        //val loginResponse = gson.fromJson(response.errorBody()!!.string(), LoginResponse::class.java)
                        mvpView?.onError(R.string.login_error_text)
                    }
                }
            }
        })
    }

    override fun isLoggedIn() {
        if(!dataManager.getToken()?.isEmpty()!!) {
            mvpView?.openMainActivity()
        }
    }
}