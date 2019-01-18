package org.timreynolds.cogni.ui.main

import android.util.Log
import org.json.JSONObject
import org.timreynolds.cogni.R
import org.timreynolds.cogni.data.DataManager
import org.timreynolds.cogni.data.VenuesResponse
import org.timreynolds.cogni.ui.base.BasePresenter
import org.timreynolds.cogni.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response





/**
 * MainPresenter
 */
class MainPresenter<T : MainMvpView>(private val dataManager: DataManager) : BasePresenter<T>(), MainMvpPresenter<T> {

    val paramObject = JSONObject()

    companion object {
        private val TAG: String = MainPresenter::class.java.simpleName
    }

    override fun getVenues() {
        if(mvpView!!.isNetworkConnected()) {
            if (dataManager!!.getToken() != null) {
                Log.i(TAG, "token -> " + dataManager.getToken())

                dataManager.getVenues(token = dataManager.getToken()!!, city = "New York").enqueue(object : Callback<VenuesResponse?> {
                    override fun onFailure(call: Call<VenuesResponse?>?, t: Throwable?) {
                        Logger.e(TAG, "Could not retrieve venues")
                        mvpView?.onError(t?.localizedMessage!!)
                    }

                    override fun onResponse(call: Call<VenuesResponse?>, response: Response<VenuesResponse?>) {
                        if (response != null && response.isSuccessful) {
                            //Log.i(TAG, "response -> " + response
                            if(response.body()?.venues != null) {
                                val myVenues = response.body()?.venues
                                mvpView?.displayVenues(myVenues)
                                for (item in myVenues!!) {
                                    Log.i(TAG, "** cashback-> " + item.cashback)
                                }
                            } else {
                                mvpView?.onError(R.string.error_get_venues)
                            }
                            Log.i(TAG, "venues received")
                        } else {
                            Log.i(TAG, "** venues list call failed **")
                            mvpView?.onError(R.string.error_get_venues)
                        }
                    }
                })
            } else {
                mvpView?.onError(R.string.error_get_venues_auth)
            }
        } else {
            mvpView?.onError(R.string.error_no_network)
        }
    }

    override fun getName(): String? {
        return dataManager.getName()
    }

    override fun getEmail(): String? {
        return dataManager.getEmail()
    }

    override fun logout() {
        dataManager.saveToken("")
        mvpView?.gotoLoginActivity()
    }

}