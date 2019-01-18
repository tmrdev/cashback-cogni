package org.timreynolds.cogni.ui.login

import org.timreynolds.cogni.ui.base.MvpView

/**
 * LoginMvpView
 */
interface LoginMvpView : MvpView{

    fun openMainActivity()

    fun showError(message: String)

}