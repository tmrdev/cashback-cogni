package org.timreynolds.cogni.ui.main

import org.timreynolds.cogni.data.Venues
import org.timreynolds.cogni.ui.base.MvpView

/**
 * MainMvpView
 */
interface MainMvpView: MvpView {

    fun gotoLoginActivity()

    fun displayVenues(myVenues: List<Venues>?)

}