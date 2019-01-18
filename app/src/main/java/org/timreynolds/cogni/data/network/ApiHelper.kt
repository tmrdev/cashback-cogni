

package org.timreynolds.cogni.data.network

import org.timreynolds.cogni.data.LoginResponse
import org.timreynolds.cogni.data.VenuesResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * ApiHelper
 */

interface ApiHelper {

    @POST("users")
    @FormUrlEncoded
    fun doSignUp(@Field("name") username: String,@Field("email") email: String) : Call<LoginResponse>

    //@Headers("Content-Type: application/json")
    @GET("venues")
    fun getVenues(@Header("token") token: String, @Query("city") city: String): Call<VenuesResponse>

    @POST("login") // not using Login accepts token only, so just using a sign up action
    @FormUrlEncoded
    fun doLogin(@Field("name") username: String, @Field("email") email: String) : Call<LoginResponse>
}
