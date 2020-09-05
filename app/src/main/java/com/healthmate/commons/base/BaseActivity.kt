package com.healthmate.common.base

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.healthmate.R
import com.healthmate.common.functions.Fun
import com.healthmate.common.navigation.Navigator
import com.healthmate.common.sharedpreferences.AppPref
import com.healthmate.common.sharedpreferences.UserPref
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.IOException

abstract class BaseActivity: AppCompatActivity() {
    val navigator = Navigator()
    abstract fun getView(): Int
    lateinit var appPref: AppPref
    lateinit var userPref: UserPref
    protected lateinit var gson: Gson
    protected var requestOptions = RequestOptions().diskCacheStrategy(
            DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appPref = AppPref(this)
        userPref = UserPref(this)
        setContentView(getView())
        observeVM()
        onActivityCreated(savedInstanceState)
        if (!Fun.isConnected(this)){
            Toast.makeText(this,"Tidak ada akses internet",Toast.LENGTH_LONG).show()
        }
    }

    abstract fun onActivityCreated(savedInstanceState: Bundle?)

    init {
        createGson()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base))
    }

    open fun observeVM(){ }

    open fun startLoading(){}
    open fun finishLoading(){}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

    protected fun createDialog(message: String, click: DialogCallback? = null,title: String = resources.getString(R.string.app_name)) {
        val dialog = MaterialDialog(this).title(null,title)
            .message(null, message)
            .positiveButton(null,"OK",click).cancelable(false)
        dialog.show()
    }

    protected fun createDialogWithBackButton(title: String,message: String, click: DialogCallback? = null, positiveText: String = "OK"){
        val dialog = MaterialDialog(this).title(null,title)
            .message(null, message)
            .positiveButton(null,positiveText,click).cancelable(false)
            .negativeButton(null,"Back",{
                it.dismiss()
            }).cancelable(true)
        dialog.show()
    }
}