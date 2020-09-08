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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.healthmate.R
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.adapter.RecyclerViewTouchListener
import com.healthmate.common.functions.Fun
import com.healthmate.menu.reusable.data.User
import com.healthmate.common.navigation.Navigator
import com.healthmate.common.sharedpreferences.AppPref
import com.healthmate.common.sharedpreferences.UserPref
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.IOException

abstract class BaseFragment: Fragment() {
    val navigator = Navigator()
    abstract fun getViewId(): Int
    lateinit var appPref: AppPref
    lateinit var userPref: UserPref
    var user = User()
    protected lateinit var gson: Gson

    init {
        createGson()
    }

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

    protected fun createDialogWithBackButton(title: String=resources.getString(R.string.app_name), message: String, click: DialogCallback? = null, positiveText: String = "OK", negativeText: String = "Kembali"){
        val dialog = MaterialDialog(activity!!).title(null,title)
                .message(null, message)
                .positiveButton(null,positiveText,click).cancelable(false)
                .negativeButton(null,negativeText,{
                    it.dismiss()
                }).cancelable(true)
        dialog.show()
    }

    protected fun createDialog(message: String, click: DialogCallback? = null,title: String=resources.getString(R.string.app_name)){
        val dialog = MaterialDialog(activity!!).title(null,title)
                .message(null, message)
                .positiveButton(null,"OK",click).cancelable(false)
        dialog.show()
    }

    private fun createGson() {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Float::class.java, object : TypeAdapter<Float>() {
            @Throws(IOException::class)
            override fun read(reader: JsonReader): Float? {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull()
                    return null
                }
                try {
                    return java.lang.Float.valueOf(reader.nextString())
                } catch (e: NumberFormatException) {
                    return null
                }

            }

            @Throws(IOException::class)
            override fun write(writer: JsonWriter, value: Float?) {
                if (value == null) {
                    writer.nullValue()
                    return
                }
                writer.value(value)
            }

        })
        gsonBuilder.registerTypeAdapter(Double::class.java, object : TypeAdapter<Double>() {
            @Throws(IOException::class)
            override fun read(reader: JsonReader): Double? {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull()
                    return 0.0
                }
                try {
                    return java.lang.Double.valueOf(reader.nextString())
                } catch (e: NumberFormatException) {
                    return 0.0
                }

            }

            @Throws(IOException::class)
            override fun write(writer: JsonWriter, value: Double?) {
                if (value == null) {
                    writer.nullValue()
                    return
                }
                writer.value(value)
            }
        })
        gson = gsonBuilder.create()
    }

    fun initiateLinearLayoutRecyclerView(recyclerView: RecyclerView, clickListener: RecyclerViewClickListener?){
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        val mLayoutManager = LinearLayoutManager(context)
        recyclerView.setLayoutManager(mLayoutManager)
        recyclerView.addOnItemTouchListener(RecyclerViewTouchListener(context!!, recyclerView, clickListener))
    }

}