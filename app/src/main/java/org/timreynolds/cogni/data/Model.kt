

package org.timreynolds.cogni.data

import com.squareup.moshi.Json
import java.io.Serializable


/**
 * LoginResponse
 */

data class LoginResponse(

        @Json(name = "user")
        var user: User

) : Serializable

data class User(

        @Json(name = "uuid")
        var uuid: String,

        @Json(name = "name")
        var name: String,

        @Json(name = "email")
        var email: String

) : Serializable


data class VenuesResponse(

        @Json(name = "venues")
        var venues: List<Venues>

) : Serializable

data class Venues (

        @Json(name = "id")
        var id: Int,

        @Json(name = "name")
        var name: String,

        @Json(name = "city")
        var city: String,

        @Json(name = "cashback")
        var cashback: Float,

        @Json(name = "lat")
        var lat: Double,

        @Json(name = "long")
        var long: Double

) : Serializable
