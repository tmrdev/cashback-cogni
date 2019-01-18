package org.timreynolds.cogni.ui.main

import org.timreynolds.cogni.ui.base.MvpPresenter
import org.timreynolds.cogni.ui.base.MvpView

/**
 * MainMvpPresenter
 */
interface MainMvpPresenter<T: MvpView> : MvpPresenter<T> {

    fun logout()

    fun getName(): String?

    fun getEmail(): String?

    fun getVenues()

}