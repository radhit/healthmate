package com.healthmate.common.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.healthmate.common.functions.Fun
import com.healthmate.menu.reusable.data.User
import com.healthmate.common.navigation.Navigator
import com.healthmate.common.sharedpreferences.AppPref
import com.healthmate.common.sharedpreferences.UserPref
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

abstract class BaseFragment: Fragment() {
    val navigator = Navigator()
    abstract fun getViewId(): Int
    lateinit var appPref: AppPref
    lateinit var userPref: UserPref
    var user = User()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appPref = AppPref(activity!!)
        userPref = UserPref(activity!!)
        user = userPref.getUser()
        observeVM()
        onFragmentCreated(savedInstanceState)
        if (!Fun.isConnected(activity!!)){
            Toast.makeText(activity!!,"Tidak ada akses internet", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getViewId(), container, false)
    }

    abstract fun onFragmentCreated(savedInstanceState: Bundle?)


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    open fun observeVM(){ }

    protected fun signout(){
        userPref.setUser(User())
        navigator.signin(activity!!,true)
    }
}