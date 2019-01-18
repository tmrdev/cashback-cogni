package org.timreynolds.cogni

import android.app.Application
import org.timreynolds.cogni.data.AppDataManager
import org.timreynolds.cogni.data.DataManager
import org.timreynolds.cogni.data.network.ApiHelper
import org.timreynolds.cogni.data.network.AppApiHelper
import org.timreynolds.cogni.data.network.RestAdapter
import org.timreynolds.cogni.data.preference.AppPreferenceHelper
import org.timreynolds.cogni.data.preference.PreferenceHelper

/**
 * MyApplication
 */
class MyApplication : Application() {

    lateinit var dataManager: DataManager
    lateinit var apiHelper: ApiHelper
    lateinit var restAdapter: RestAdapter
    lateinit var prefHelper: PreferenceHelper

    companion object {
        val FILE_NAME: String = "kotlin_sample"
    }

    override fun onCreate() {
        super.onCreate()
        // replace with DI like Dagger2 or Koin
        restAdapter = RestAdapter()
        apiHelper = AppApiHelper(restAdapter)
        prefHelper = AppPreferenceHelper(applicationContext, FILE_NAME)
        dataManager = AppDataManager(apiHelper, prefHelper)
    }

}