

package org.timreynolds.cogni.data.network

import org.timreynolds.cogni.data.LoginResponse
import org.timreynolds.cogni.data.VenuesResponse
import retrofit2.Call

/**
 * AppApiHelper
 */
class AppApiHelper(private var mRestAdapter: RestAdapter) : ApiHelper {

    override fun getVenues(token: String, city: String): Call<VenuesResponse> {
        return mRestAdapter.apiHelper.getVenues(token, city)
    }

    override fun doSignUp(username: String, email: String): Call<LoginResponse> {
        return mRestAdapter.apiHelper.doSignUp(username, email)
    }

    override fun doLogin(username: String, email: String): Call<LoginResponse> {
        return mRestAdapter.apiHelper.doLogin(username, email)
    }


}
