package org.timreynolds.cogni.data.preference

/**
 * PreferenceHelper
 */
interface PreferenceHelper {

    fun getToken(): String?

    fun saveToken(token: String)

    fun saveName(name: String)

    fun saveEmail(email: String)

    fun getName(): String?

    fun getEmail(): String?

}