package com.healthmate.menu.mom.kia.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.Fun
import com.healthmate.di.injector
import kotlinx.android.synthetic.main.activity_main_kia.*
import java.util.*
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.healthmate.BuildConfig
import com.healthmate.api.*
import com.healthmate.menu.reusable.data.*
import kotlinx.android.synthetic.main.activity_main_kia.fieldNama
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import com.healthmate.api.Result
import com.healthmate.common.functions.replaceEmpty
import kotlinx.android.synthetic.main.activity_main_kia.fieldNomorHp
import kotlinx.android.synthetic.main.activity_main_kia.fieldPassword
import kotlinx.android.synthetic.main.activity_signin.*
import java.time.LocalDateTime

class MainKiaActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        val EXTRA_KETERANGAN = "EXTRA_KETERANGAN"
        val RC_GALLERY = 102
        val RC_CAMERA = 103
        val RP_WRITE_STORAGE = 200
        val RP_CAMERA = 201
        val RP_GALLERY = 202

        @JvmStatic
        fun getCallingIntent(activity: Activity, keterangan: String, data: String): Intent {
            val intent = Intent(activity, MainKiaActivity::class.java)
            intent.putExtra(EXTRA,data)
            intent.putExtra(EXTRA_KETERANGAN, keterangan)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_main_kia
    var dataKia: Kia = Kia()
    var city: Location = Location()
    var district: Location = Location()
    //Gambar
    lateinit var gambar_bitmap: Bitmap
    lateinit var imageUri: Uri
    var changeImage: Boolean = false
    lateinit var user : User

    var keterangan = ""
    var dateHusband: String = ""
    var dateMother: String = ""


    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.masterVM()).get(MasterViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Data KIA")

        keterangan = intent.getStringExtra(EXTRA_KETERANGAN)
        if (keterangan.equals("mother")){
            fieldNama.setText("${userPref.getUser().name}")
            Glide.with(this).applyDefaultRequestOptions(requestOptionsMom).load(userPref.getUser().profile_picture).into(iv_profile)

            if (userPref.getUser().kia!=null){
                dataKia = userPref.getUser().kia!!
                setData()
            }
            user = userPref.getUser()
            midwife_create.visibility = View.GONE
        } else if (keterangan.equals("midwife_create")){
            user = User()
            midwife_create.visibility = View.VISIBLE
        } else if (keterangan.equals("midwife_edit")){
            user = gson.fromJson(intent.getStringExtra(EXTRA),User::class.java)
            dataKia = user.kia!!
            fieldNama.setText("${user.name}")
            setData()
            midwife_create.visibility = View.GONE
            Glide.with(this).applyDefaultRequestOptions(requestOptionsMom).load(user.profile_picture).into(iv_profile)
        }
        btn_simpan.setOnClickListener {
            if (isValid()){
                setDataInput()
                if (keterangan.equals("midwife_create")){
                    user.phone_number = fieldNomorHp.text.toString()
                    user.password = Base64.getEncoder().encodeToString(fieldPassword.text.toString().toByteArray())
                }
                user.name = fieldNama.text.toString()
                user.kia = dataKia
                user.city = city
                user.district = district
                println("data user kia : ${gson.toJson(user)}")
                if (changeImage){
                    uploadFoto()
                } else{
                    if (keterangan.equals("mother")){
                        updateData()
                    } else{
                        createData()
                    }
                }
            }
        }
        fieldTanggalLahir.setOnClickListener {
            callCalender("mother")
        }
        fieldTanggalLahirSuami.setOnClickListener {
            callCalender("husband")
        }
        fieldKabupaten.setOnClickListener {
            navigator.listLocation(this, "kabupaten",1)
        }
        fieldKecamatan.setOnClickListener {
            navigator.listLocation(this, "kecamatan",2)
        }
        fieldGoldarMom.setOnClickListener {
            navigator.dataMaster(this,"goldar",3)
        }
        fieldGoldarSuami.setOnClickListener {
            navigator.dataMaster(this,"goldar",4)
        }
        
        fieldPendidikan.setOnClickListener {
            navigator.dataMaster(this, "pendidikan",6)
        }
        fieldAgamaMom.setOnClickListener {
            navigator.dataMaster(this, "agama",7)
        }
        fieldAgamaSuami.setOnClickListener {
            navigator.dataMaster(this,"agama",8)
        }
        fieldPendidikanSuami.setOnClickListener {
            navigator.dataMaster(this, "pendidikan",9)
        }
        fieldPenolong.setOnClickListener {
            navigator.dataMaster(this,"penolong persalinan",10)
        }
        fieldDana.setOnClickListener {
            navigator.dataMaster(this,"dana persalinan",11)
        }
        fieldKendaraan.setOnClickListener {
            navigator.dataMaster(this,"kendaraan",12)
        }
        fieldMetode.setOnClickListener {
            navigator.dataMaster(this, "metode kb",13)
        }
        rl_foto.setOnClickListener {
            createDialogTakePhoto()
        }
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
                            PayloadEntryMultipart("image","profile_${userPref.getUser().id}.jpg",
                                    RequestBody.create(MediaType.parse("image/jpg"), byteArray))
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
                                user.profile_picture = freshData.url
                                if (keterangan.equals("mother")){
                                    updateData()
                                } else{
                                    createData()
                                }
                            }
                            Result.Status.ERROR->{
                                closeLoadingDialog()
                                createDialog(result.message!!)
                            }
                        }
                    })
        }catch (e: UninitializedPropertyAccessException){
            e.printStackTrace()
            createDialog(e.localizedMessage)
        }
    }

    fun updateData(){
        startLoading()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(user))
        var url = ""
        url = "${Urls.registerMother}/${userPref.getUser().id}"
//        if (keterangan.equals("mother")){
//        }
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.updateDataMom(url,requestBody)
        call.enqueue(object : Callback<DataResponse<Any>> {
            override fun onResponse(call: Call<DataResponse<Any>>?, response: Response<DataResponse<Any>>?) {
                finishLoading()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299){
                        if (keterangan.equals("mother")){
                            userPref.setUser(user)
                        }
                        finish()
                    } else{
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@MainKiaActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                finishLoading()
                Toast.makeText(this@MainKiaActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun createData(){
        startLoading()
        lateinit var requestBody: RequestBody
        var url = ""
        if (keterangan.equals("midwife_create")){
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(user))
            url = Urls.registerMother
        } else{
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(user.kia))
            url = "${Urls.registerMother}/${user.id}/kia"
        }
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.createDataMom(url,requestBody)
        call.enqueue(object : Callback<DataResponse<Any>> {
            override fun onResponse(call: Call<DataResponse<Any>>?, response: Response<DataResponse<Any>>?) {
                finishLoading()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299){
                        if (keterangan.equals("mother")){
                            userPref.setUser(user)
                        }
                        finish()
                    } else{
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@MainKiaActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                finishLoading()
                Toast.makeText(this@MainKiaActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun isValid(): Boolean {
        if (fieldNama.text.toString().equals("")){
            fieldNama.setError("Wajib diisi!")
            return false
        } else if (fieldDomisili.text.toString().equals("")){
            fieldDomisili.setError("Wajib diisi!")
            return false
        } else if (fieldNomorKtp.text.toString().equals("")){
            fieldNomorKtp.setError("Wajib diisi!")
            return false
        } else if (fieldTempatLahir.text.toString().equals("")){
            fieldTempatLahir.setError("Wajib diisi!")
            return false
        } else if (fieldTanggalLahir.text.toString().equals("")){
            fieldTanggalLahir.setError("Wajib diisi!")
            return false
        } else if (fieldHamilKeberapa.text.toString().equals("")){
            fieldHamilKeberapa.setError("Wajib diisi!")
            return false
        } else if (fieldUmurAnak.text.toString().equals("")){
            fieldUmurAnak.setError("Isi 0 jika tidak ada")
            return false
        } else if (fieldPekerjaanMom.text.toString().equals("")){
            fieldPekerjaanMom.setError("Wajib diisi!")
            return false
        } else if (fieldAgamaMom.text.toString().equals("")){
            fieldAgamaMom.setError("Wajib diisi!")
            return false
        } else if (fieldNomorJkn.text.toString().equals("")){
            fieldNomorJkn.setError("Wajib diisi!")
            return false
        } else if (fieldGoldarMom.text.toString().equals("")){
            fieldGoldarMom.setError("Wajib diisi!")
            return false
        } else if (fieldNamaSuami.text.toString().equals("")){
            fieldNamaSuami.setError("Wajib diisi!")
            return false
        } else if (fieldTempatLahirSuami.text.toString().equals("")){
            fieldTempatLahirSuami.setError("Wajib diisi!")
            return false
        } else if (fieldTanggalLahirSuami.text.toString().equals("")){
            fieldTanggalLahirSuami.setError("Wajib diisi!")
            return false
        } else if (fieldNomorHpSuami.text.toString().equals("")){
            fieldNomorHpSuami.setError("Wajib diisi!")
            return false
        } else if (fieldAgamaSuami.text.toString().equals("")){
            fieldAgamaSuami.setError("Wajib diisi!")
            return false
        } else if (fieldGoldarSuami.text.toString().equals("")){
            fieldGoldarSuami.setError("Wajib diisi!")
            return false
        } else if (fieldAlamat.text.toString().equals("")){
            fieldAlamat.setError("Wajib diisi!")
            return false
        } else if (fieldKabupaten.text.toString().equals("")){
            fieldKabupaten.setError("Wajib diisi!")
            return false
        } else if (fieldKecamatan.text.toString().equals("")){
            fieldKecamatan.setError("Wajib diisi!")
            return false
        }
        if (keterangan.equals("midwife_create")){
            if (fieldNomorHp.text.toString().equals("")){
                fieldNomorHp.setError("Wajib diisi")
                return false
            } else if (fieldPassword.text.toString().equals("")){
                fieldPassword.setError("Wajib disii")
                return false
            }
        }
        return true
    }

    private fun setDataInput() {
        dataKia.domisili = fieldDomisili.text.toString()
        dataKia.nik = fieldNomorKtp.text.toString()
        dataKia.birth_place = fieldTempatLahir.text.toString()
        dataKia.birth_date = dateMother
        dataKia.number_of_pregnancy = fieldHamilKeberapa.text.toString()
        dataKia.last_child_age = fieldUmurAnak.text.toString()
        dataKia.last_education = fieldPendidikan.text.toString()
        dataKia.job = fieldPekerjaanMom.text.toString()
        dataKia.religion = fieldAgamaMom.text.toString()
        dataKia.jkn_number = fieldNomorJkn.text.toString()
        dataKia.blood_type = fieldGoldarMom.text.toString()
        city.address = fieldAlamat.text.toString()
        district.address = fieldAlamat.text.toString()
        var husband = Husband(fieldNamaSuami.text.toString(),fieldTempatLahirSuami.text.toString(),dateHusband,fieldNomorHpSuami.text.toString(),
                fieldAgamaSuami.text.toString(),fieldGoldarSuami.text.toString(),fieldPekerjaanSuami.text.toString(),fieldPendidikanSuami.text.toString(),
                fieldAlamat.text.toString(),city,district)
        dataKia.husband = husband

        var persalinan = Persalinan(fieldPenolong.text.toString().replaceEmpty(""),fieldDana.text.toString().replaceEmpty("")
                ,fieldKendaraan.text.toString().replaceEmpty(""),fieldMetode.text.toString().replaceEmpty(""), fieldDonor.text.toString().replaceEmpty("")
                ,fieldNomorPendonor.text.toString().replaceEmpty(""))
        dataKia.persalinan = persalinan

    }

    private fun setData() {
        fieldDomisili.setText("${dataKia.domisili}")
        fieldNomorKtp.setText("${dataKia.nik}")
        fieldTempatLahir.setText("${dataKia.birth_place}")
        fieldTanggalLahir.setText("${dataKia.birth_date.split("T")[0]}")
        fieldHamilKeberapa.setText("${dataKia.number_of_pregnancy}")
        fieldUmurAnak.setText("${dataKia.last_child_age}")
        fieldPendidikan.setText("${dataKia.last_education}")
        fieldPekerjaanMom.setText("${dataKia.job}")
        fieldAgamaMom.setText("${dataKia.religion}")
        fieldNomorJkn.setText("${dataKia.jkn_number}")
        fieldGoldarMom.setText("${dataKia.blood_type}")
        if (dataKia.husband!=null){
            fieldNamaSuami.setText("${dataKia.husband!!.name}")
            fieldTempatLahirSuami.setText("${dataKia.husband!!.birth_place}")
            fieldTanggalLahirSuami.setText("${dataKia.husband!!.birth_date.split("T")[0]}")
            fieldNomorHpSuami.setText("${dataKia.husband!!.phone_number}")
            fieldAgamaSuami.setText("${dataKia.husband!!.religion}")
            fieldGoldarSuami.setText("${dataKia.husband!!.blood_type}")
            fieldAlamat.setText("${dataKia.husband!!.address}")
            fieldKabupaten.setText("${dataKia.husband!!.city!!.name}")
            fieldKecamatan.setText("${dataKia.husband!!.district!!.name}")
            fieldPendidikanSuami.setText("${dataKia.husband!!.last_education}")
            fieldPekerjaanSuami.setText("${dataKia.husband!!.job}")
            city = dataKia.husband!!.city!!
            district = dataKia.husband!!.district!!
        }
        if (dataKia.persalinan!=null){
            fieldPenolong.setText("${dataKia.persalinan!!.helper}")
            fieldDana.setText("${dataKia.persalinan!!.funds}")
            fieldKendaraan.setText("${dataKia.persalinan!!.vehicle}")
            fieldMetode.setText("${dataKia.persalinan!!.metode_kb}")
            fieldDonor.setText("${dataKia.persalinan!!.blood_donor}")
            fieldNomorPendonor.setText("${dataKia.persalinan!!.blood_kontak}")
        }
    }

    private fun callCalender(keterangan: String) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var tanggal = ""
                    tanggal = year.toString() + "-"
                    if (monthOfYear + 1 < 10) {
                        tanggal = tanggal + "0" + (monthOfYear + 1).toString() + "-"
                    } else {
                        tanggal = tanggal + (monthOfYear + 1).toString() + "-"
                    }

                    if (dayOfMonth < 10) {
                        tanggal = tanggal + "0" + dayOfMonth.toString()
                    } else {
                        tanggal = tanggal + dayOfMonth.toString()
                    }
                    if (keterangan.equals("mother")){
                        fieldTanggalLahir.setText(tanggal)
                        dateMother = "${tanggal}T01:00:00+07:00"
                    } else{
                        fieldTanggalLahirSuami.setText(tanggal)
                        dateHusband = "${tanggal}T01:00:00+07:00"
                    }
                }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.maxDate = Date().time
        datePickerDialog.show()
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
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldGoldarMom.setText(dataMaster.name)
            }
        } else if (requestCode==4){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldGoldarSuami.setText(dataMaster.name)
            }
        } else if (requestCode==5){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldPekerjaanMom.setText(dataMaster.name)
            }
        } else if (requestCode==6){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldPendidikan.setText(dataMaster.name)
            }
        } else if (requestCode==7){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldAgamaMom.setText(dataMaster.name)
            }
        } else if (requestCode==8){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldAgamaSuami.setText(dataMaster.name)
            }
        } else if (requestCode== RC_CAMERA){
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
        } else if (requestCode== RC_GALLERY){
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
        } else if (requestCode==9){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldPendidikanSuami.setText(dataMaster.name)
            }
        } else if (requestCode==10){
            var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
            fieldPenolong.setText(dataMaster.name)
        } else if (requestCode==11){
            var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
            fieldDana.setText(dataMaster.name)
        } else if (requestCode==12){
            var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
            fieldKendaraan.setText(dataMaster.name)
        } else if (requestCode==13){
            var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
            fieldMetode.setText(dataMaster.name)
        }
    }

    override fun startLoading() {
        super.startLoading()
        btn_simpan.isEnabled = false
        btn_simpan.text = "Mohon Tunggu..."
    }

    override fun finishLoading() {
        super.finishLoading()
        btn_simpan.isEnabled = true
        btn_simpan.text = "Simpan"
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
                    RP_GALLERY
            )
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent,
                        RC_GALLERY
                )
            }
        }
    }

    fun openCamera() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(android.Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, permissions,
                    RP_CAMERA
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
                                    RC_CAMERA
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