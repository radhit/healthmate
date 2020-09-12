package com.healthmate.menu.midwife.profile.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.healthmate.BuildConfig
import com.healthmate.R
import com.healthmate.api.*
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.Fun
import com.healthmate.di.injector
import com.healthmate.menu.auth.view.SigninActivity
import com.healthmate.menu.mom.kia.view.MainKiaActivity
import com.healthmate.menu.reusable.data.Hospital
import com.healthmate.menu.reusable.data.Location
import com.healthmate.menu.reusable.data.MasterViewModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_ubah_profile_midwive.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.ArrayList

class UbahProfileMidwiveActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        val RC_GALLERY = 102
        val RC_CAMERA = 103
        val RP_WRITE_STORAGE = 200
        val RP_CAMERA = 201
        val RP_GALLERY = 202
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            val intent = Intent(activity, UbahProfileMidwiveActivity::class.java)
            return intent
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.masterVM()).get(MasterViewModel::class.java)
    }

    override fun getView(): Int = R.layout.activity_ubah_profile_midwive
    var hospital = Hospital()
    var city = Location()
    var district = Location()
    lateinit var user : User

    //Gambar
    lateinit var gambar_bitmap: Bitmap
    lateinit var imageUri: Uri
    var changeImage: Boolean = false


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Ubah Profile Bidan")
        setData()
        fieldKabupaten.setOnClickListener {
            navigator.listLocation(this, "kabupaten",1)
        }
        fieldKecamatan.setOnClickListener {
            navigator.listLocation(this, "kecamatan",2)
        }
        fieldHospital.setOnClickListener {
            if (userPref.getUser().city!!.name.equals("") || userPref.getUser().district!!.name.equals("")){
                createDialog("Kota dan kecamatan kerja belum dipilih")
            } else{
                navigator.listLocation(this,"bidan",3)
            }
        }
        rl_foto.setOnClickListener {
            createDialogTakePhoto()
        }
        btn_simpan.setOnClickListener {
            if (isValid()){
                user = userPref.getUser()
                user.name = fieldNama.text.toString()
                user.phone_number = fieldNomorHp.text.toString()
                user.str_number = fieldNomorStr.text.toString()
                user.city = city
                user.district = district
                user.hospital = hospital
                if (changeImage){
                    uploadFoto()
                } else{
                    updateData()
                }
            }
        }
    }

    private fun setData() {
        fieldNama.setText("${userPref.getUser().name}")
        fieldNomorHp.setText("${userPref.getUser().phone_number}")
        fieldNomorStr.setText("${userPref.getUser().str_number!!}")
        if (!userPref.getUser().hospital!!.id.equals("")){
            fieldHospital.setText("${userPref.getUser().hospital!!.name}")
            hospital = userPref.getUser().hospital!!
        }
        if (!userPref.getUser().city!!.id.equals("")){
            fieldKabupaten.setText("${userPref.getUser().city!!.name}")
            city = userPref.getUser().city!!
        }
        if (!userPref.getUser().district!!.id.equals("")){
            fieldKecamatan.setText("${userPref.getUser().district!!.name}")
            district = userPref.getUser().district!!
        }
        Glide.with(this).applyDefaultRequestOptions(requestOptionsMidwife).load(userPref.getUser().profil_picture).into(iv_profile)

    }

    private fun uploadFoto() {
        try{
            val stream = ByteArrayOutputStream()
            gambar_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            val byteArray = stream.toByteArray()
//            formBodyMultipart.addFormDataPart("image", "produk", RequestBody.create(MediaType.parse("image/jpeg"), byteArray))
            val payload = Payload(
                    ArrayList(listOf(PayloadEntry("image","image"))),
                    ArrayList(listOf(
                            PayloadEntryMultipart("image","image",
                                    RequestBody.create(MediaType.parse("image/jpeg"), byteArray))
                    ))
            )
            viewModel.uploadFoto(payload)
                    .observe(this, Observer {result ->
                        when(result.status){
                            Result.Status.LOADING->{
                                showLoadingDialog()
                            }
                            Result.Status.SUCCESS->{
                                closeLoadingDialog()
                                val freshData = result.data!!
                                var user = userPref.getUser()
                                user.profil_picture = freshData.url
                                userPref.setUser(user)
                                updateData()
                            }
                            Result.Status.ERROR->{
                                closeLoadingDialog()
                                Fun.handleError(this,result)
                            }
                        }
                    })
        }catch (e: UninitializedPropertyAccessException){
            e.printStackTrace()
            createDialog(e.localizedMessage)
        }
    }

    private fun updateData() {
        startLoading()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(userPref.getUser()))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.updateDataMidwive("${Urls.registerMidwife}/${userPref.getUser().id}",requestBody)
        call.enqueue(object : Callback<DataResponse<Any>> {
            override fun onResponse(call: Call<DataResponse<Any>>?, response: Response<DataResponse<Any>>?) {
                finishLoading()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299){
                        userPref.setUser(user)
                        createDialog(response.body()!!.message,{
                            finish()
                        })
                    } else{
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@UbahProfileMidwiveActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                finishLoading()
                Toast.makeText(this@UbahProfileMidwiveActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun isValid(): Boolean {
        if (fieldNama.text.toString().equals("")){
            fieldNama.setError("Wajib diisi")
            return false
        } else if (fieldNomorHp.text.toString().equals("")){
            fieldNomorHp.setError("Wajib diisi")
            return false
        } else if (fieldNomorStr.text.toString().equals("")){
            fieldNomorStr.setError("Wajib diisi")
            return false
        } else if (fieldKabupaten.text.toString().equals("")){
            fieldKabupaten.setError("Wajib diisi")
            return false
        } else if (fieldKecamatan.text.toString().equals("")){
            fieldKecamatan.setError("Wajib diisi")
            return false
        } else if (fieldHospital.text.toString().equals("")){
            fieldHospital.setError("Wajib diisi")
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),Location::class.java)
                fieldKabupaten.setText(dataMaster.name)
                city = dataMaster
            }
        } else if (requestCode==2){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),Location::class.java)
                fieldKecamatan.setText(dataMaster.name)
                district = dataMaster
            }
        } else if (requestCode==3){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), Hospital::class.java)
                fieldHospital.setText(dataMaster.name)
                hospital = dataMaster
            }
        } else if (requestCode== MainKiaActivity.RC_CAMERA){
            var bitmap: Bitmap? = null
            try {
                bitmap = data?.extras?.get("data") as Bitmap
            } catch (e: RuntimeException) {
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                try {
                    mediaScanIntent.data = imageUri
                    launchMediaScanIntent(mediaScanIntent)
                    try {
                        bitmap = decodeBitmapUri(this, imageUri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        createDialog(e.localizedMessage)
                    }
                } catch (e: UninitializedPropertyAccessException) {
                    e.printStackTrace()
                    createDialog(e.localizedMessage)
                }

            }
            if (bitmap != null) {
                Glide.with(this).applyDefaultRequestOptions(requestOptions).load(bitmap).into(iv_profile)
                gambar_bitmap = bitmap
                iv_profile.visibility = View.VISIBLE
                changeImage = true
            }
        } else if (requestCode== MainKiaActivity.RC_GALLERY){
            try {
                val returnUri = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), returnUri)
                gambar_bitmap = bitmap
                Glide.with(this).applyDefaultRequestOptions(requestOptions).load(bitmap).into(iv_profile)
                gambar_bitmap = bitmap
                changeImage = true
            }catch (e: java.lang.NullPointerException){
                e.printStackTrace()
                createDialog(e.localizedMessage)
            }
        }
    }

    protected fun createDialogTakePhoto(){
        val dialog = MaterialDialog(this).title(null,"Ambil Gambar")
                .message(null, "Pilih sumber gambar")
                .positiveButton(null,"Camera",{
                    it.dismiss()
                    openCamera()
                })
                .negativeButton(null,"Gallery",{
                    it.dismiss()
                    openGallery()
                }).noAutoDismiss().cancelable(true)
        dialog.show()
    }

    fun openGallery() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permissions,
                    UbahProfileMidwiveActivity.RP_GALLERY
            )
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent,
                        UbahProfileMidwiveActivity.RC_GALLERY
                )
            }
        }
    }

    fun openCamera() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(android.Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, permissions,
                    UbahProfileMidwiveActivity.RP_CAMERA
            )
        } else {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    val applicationId = BuildConfig.APPLICATION_ID
                    photoFile?.also {
                        imageUri = FileProvider.getUriForFile(
                                this,
                                "$applicationId.provider",
                                it
                        )
                        if (::imageUri.isInitialized){
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                            startActivityForResult(takePictureIntent,
                                    UbahProfileMidwiveActivity.RC_CAMERA
                            )
                        }
                    }
                }
            }
        }
    }

    fun launchMediaScanIntent(mediaScanIntent: Intent) {
        this.sendBroadcast(mediaScanIntent)
    }

    @Throws(FileNotFoundException::class)
    fun decodeBitmapUri(ctx: Context, uri: Uri?): Bitmap? {
        val targetW = 600
        val targetH = 600
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(ctx.contentResolver.openInputStream(uri), null, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight
        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        return BitmapFactory.decodeStream(ctx.contentResolver.openInputStream(uri), null, bmOptions)
    }
}