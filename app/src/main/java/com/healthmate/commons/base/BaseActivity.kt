package com.healthmate.common.base

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.healthmate.api.BaseApi
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.adapter.RecyclerViewTouchListener
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.Fun
import com.healthmate.common.navigation.Navigator
import com.healthmate.common.sharedpreferences.AppPref
import com.healthmate.common.sharedpreferences.UserPref
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

abstract class BaseActivity: AppCompatActivity() {
    protected var materialDialog: MaterialDialog? = null
    val navigator = Navigator()
    abstract fun getView(): Int
    lateinit var appPref: AppPref
    lateinit var userPref: UserPref
    lateinit var baseApi: BaseApi
    lateinit var retrofit: Retrofit
    protected lateinit var gson: Gson
    protected var requestOptions = RequestOptions().diskCacheStrategy(
            DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true)
    protected var requestOptionsMom = RequestOptions().placeholder(R.drawable.bumil_on).error(R.drawable.bumil_off).diskCacheStrategy(
            DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true)
    protected var requestOptionsMidwife = RequestOptions().placeholder(R.drawable.bidan_on).error(R.drawable.bidan_off).diskCacheStrategy(
            DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appPref = AppPref(this)
        userPref = UserPref(this)
        createRetrofit()
        baseApi = retrofit.create(BaseApi::class.java)
        setContentView(getView())
        observeVM()
        onActivityCreated(savedInstanceState)
        if (!Fun.isConnected(this)){
            Toast.makeText(this,"Tidak ada akses internet",Toast.LENGTH_LONG).show()
        }
    }

    private fun createRetrofit() {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY

        var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
                .readTimeout(2000000, TimeUnit.SECONDS)
                .writeTimeout(200000, TimeUnit.SECONDS)
                .connectTimeout(200000, TimeUnit.SECONDS)
        httpClient.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response? {
                val request: Request = chain.request().newBuilder().addHeader("Authorization", "Bearer ${userPref.getUser().token}").build()
                return chain.proceed(request)
            }
        })
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging)
//        httpClient = enableTls12OnPreLollipop(httpClient)

        retrofit = Retrofit.Builder()
                .baseUrl(Urls.SERVER_PROD)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
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

    var currentPhotoPath: String = ""
    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    protected fun showLoadingDialog(title: String = "Mohon tunggu...",isCancelable: Boolean = false) {
        if (materialDialog==null){
            materialDialog = MaterialDialog(this)
                    .title(null,title)
                    .message(null,"")
                    .noAutoDismiss()
                    .cancelable(isCancelable)
            if (isCancelable){
                materialDialog = materialDialog?.negativeButton(null,"Kembali",{
                    finish()
                    it.dismiss()
                })
            }

        }
        materialDialog?.show()
    }

    protected fun closeLoadingDialog() {
        materialDialog?.dismiss()
    }

    fun initiateLinearLayoutRecyclerView(recyclerView: RecyclerView, clickListener: RecyclerViewClickListener?){
        recyclerView.layoutManager = LinearLayoutManager(this)
        val mLayoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(mLayoutManager)
        recyclerView.addOnItemTouchListener(RecyclerViewTouchListener(this, recyclerView, clickListener))
    }
}