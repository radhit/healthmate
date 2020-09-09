package com.healthmate.menu.mom.kia.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.Fun
import com.healthmate.di.injector
import com.healthmate.menu.mom.covid.view.ScreeningCovidActivity
import com.healthmate.menu.mom.home.data.BerandaViewModel
import com.healthmate.menu.mom.kia.data.KiaViewModel
import kotlinx.android.synthetic.main.activity_main_kia.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer
import com.healthmate.api.*
import com.healthmate.menu.reusable.data.*
import okhttp3.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainKiaActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            val intent = Intent(activity, MainKiaActivity::class.java)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_main_kia
    var dataKia: Kia = Kia()
    var city: Location = Location()
    var district: Location = Location()

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.kiaVM()).get(KiaViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Data KIA")
        if (userPref.getUser().kia!=null){
            dataKia = userPref.getUser().kia!!
            setData()
        }
        fieldNama.setText("${userPref.getUser().name}")
        btn_simpan.setOnClickListener {
            if (isValid()){
                var user = userPref.getUser()
                setDataInput()
                user.name = fieldNama.text.toString()
                user.kia = dataKia
                user.city = city
                user.district = district

                userPref.setUser(user)

                updateData()
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
        fieldPekerjaanMom.setOnClickListener {
            navigator.dataMaster(this, "pekerjaan",5)
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

    }

    fun updateData(){
        startLoading()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(userPref.getUser()))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.updateDataMom("${Urls.registerMother}/${userPref.getUser().id}",requestBody)
        call.enqueue(object : Callback<DataResponse<Any>> {
            override fun onResponse(call: Call<DataResponse<Any>>?, response: Response<DataResponse<Any>>?) {
                finishLoading()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299){
                        createDialog(response.body()!!.message,{
                            finish()
                        })
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
        if (fieldNomorKtp.text.toString().equals("")){
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
        return true
    }

    private fun setDataInput() {
        dataKia.nik = fieldNomorKtp.text.toString()
        dataKia.birth_place = fieldTempatLahir.text.toString()
        dataKia.birth_date = fieldTanggalLahir.text.toString()
        dataKia.number_of_pregnancy = fieldHamilKeberapa.text.toString()
        dataKia.last_child_age = fieldUmurAnak.text.toString()
        dataKia.last_education = fieldPendidikan.text.toString()
        dataKia.job = fieldPekerjaanMom.text.toString()
        dataKia.religion = fieldAgamaMom.text.toString()
        dataKia.jkn_number = fieldNomorJkn.text.toString()
        dataKia.blood_type = fieldGoldarMom.text.toString()
        city.address = fieldAlamat.text.toString()
        district.address = fieldAlamat.text.toString()
        var husband = Husband(fieldNamaSuami.text.toString(),fieldTempatLahirSuami.text.toString(),fieldTanggalLahirSuami.text.toString(),fieldNomorHpSuami.text.toString(),
        fieldAgamaSuami.text.toString(),fieldGoldarSuami.text.toString(),"","",fieldAlamat.text.toString(),city,district)
        dataKia.husband = husband
    }

    private fun setData() {
        fieldNomorKtp.setText("${dataKia.nik}")
        fieldTempatLahir.setText("${dataKia.birth_place}")
        fieldTanggalLahir.setText("${dataKia.birth_date}")
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
            fieldTanggalLahirSuami.setText("${dataKia.husband!!.birth_date}")
            fieldNomorHpSuami.setText("${dataKia.husband!!.phone_number}")
            fieldAgamaSuami.setText("${dataKia.husband!!.religion}")
            fieldGoldarSuami.setText("${dataKia.husband!!.blood_type}")
            fieldAlamat.setText("${dataKia.husband!!.address}")
            fieldKabupaten.setText("${dataKia.husband!!.city!!.name}")
            fieldKecamatan.setText("${dataKia.husband!!.district!!.name}")
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
                    } else{
                        fieldTanggalLahirSuami.setText(tanggal)
                    }
                }, mYear, mMonth, mDay)

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
}