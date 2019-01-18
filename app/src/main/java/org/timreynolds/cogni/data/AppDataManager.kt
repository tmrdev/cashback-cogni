package org.timreynolds.cogni.data

import org.timreynolds.cogni.data.network.ApiHelper
import org.timreynolds.cogni.data.preference.PreferenceHelper
import retrofit2.Call

/**
 * AppDataManager
 */
class AppDataManager (private val apiHelper: ApiHelper, private val preferenceHelper: PreferenceHelper): DataManager {


    override fun getVenues(token: String, city: String): Call<VenuesResponse> {
        return apiHelper.getVenues(token, city)
    }

    override fun doSignUp(username: String, email: String): Call<LoginResponse> {
        return apiHelper.doSignUp(username, email)
    }

    override fun doLogin(username: String, email: String): Call<LoginResponse> {
        return apiHelper.doLogin(username, email)
    }

    override fun getToken(): String? {
        return preferenceHelper.getToken()
    }

    override fun saveToken(token: String) {
        preferenceHelper.saveToken(token)
    }

    override fun getName(): String? {
        return preferenceHelper.getName()
    }

    override fun saveName(name: String) {
        preferenceHelper.saveName(name)
    }

    override fun getEmail(): String? {
        return preferenceHelper.getEmail()
    }

    override fun saveEmail(email: String) {
        preferenceHelper.saveEmail(email)

    }

}