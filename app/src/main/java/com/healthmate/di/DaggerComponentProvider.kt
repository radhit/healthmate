package com.artcak.starter.di

import android.app.Activity
import androidx.fragment.app.Fragment

interface DaggerComponentProvider {
    val component: AppComponent
}
val Activity.injector get() = (application as DaggerComponentProvider).component

val Fragment.injector get() = (activity!!.application as DaggerComponentProvider).component