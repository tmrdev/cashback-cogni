package org.timreynolds.cogni.ui.base

/**
 * MvpPresenter
 */
interface MvpPresenter<V : MvpView> {

    fun onAttach(mvpView: V)

    fun onDetach()

}