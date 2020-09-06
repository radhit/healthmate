package com.healthmate.common.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable


open class BaseViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    protected fun getComposite() = compositeDisposable

    protected fun isSuccess(responseCode: Int): Boolean = responseCode in 200..299

    protected fun getErrorMessage(errors: List<String>): String{
        var errorString = ""
        for (x in 0..errors.size-1){
            if (x<errors.size-1) errorString += errors[x]+","
            else errorString += errors[x]
        }
        if (errorString.equals("")) return "Terjadi kesalahan"
        else return errorString
    }
}