package org.timreynolds.cogni.ui.base

/**
 * BasePresenter
 */
open class BasePresenter<V : MvpView> : MvpPresenter<V> {

    var mvpView: V? = null

    override fun onAttach(mvpView: V) {
        this.mvpView = mvpView
    }

    override fun onDetach() {
        mvpView = null
    }

}