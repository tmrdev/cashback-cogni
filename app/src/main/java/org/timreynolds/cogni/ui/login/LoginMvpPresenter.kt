package org.timreynolds.cogni.ui.login

import org.timreynolds.cogni.ui.base.MvpPresenter
import org.timreynolds.cogni.ui.base.MvpView

/**
 * LoginMvpPresenter
 */
interface LoginMvpPresenter<V: MvpView> : MvpPresenter<V> {

    fun doLogin(username : String, email: String)

    fun doSignUp(username : String, email: String)

    fun isLoggedIn()

}