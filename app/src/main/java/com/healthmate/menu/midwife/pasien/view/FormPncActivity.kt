package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.healthmate.R
import com.healthmate.api.DataResponse
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.menu.midwife.pasien.data.PncModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_form_pnc.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FormPncActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String): Intent {
            val intent = Intent(activity, FormPncActivity::class.java)
            intent.putExtra(EXTRA,data)
            return intent
        }
    }

    override fun getView(): Int = R.layout.activity_form_pnc
    var dataMother: User = User()
    var pncModel: PncModel = PncModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        dataMother = gson.fromJson(intent.getStringExtra(EXTRA),User::class.java)
        btn_simpan.setOnClickListener {
            if (isValid()){
                setDataInput()
                submit()
            }
        }
        fieldTanggal.setOnClickListener {
            callCalender()
        }
    }

    private fun submit() {
        showLoadingDialog()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(pncModel))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.inputForm("${Urls.pnc}",requestBody)
        call.enqueue(object : Callback<DataResponse<Any>> {
            override fun onResponse(call: Call<DataResponse<Any>>?, response: Response<DataResponse<Any>>?) {
                closeLoadingDialog()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299){
                        finish()
                    } else{
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@FormPncActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormPncActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setDataInput() {
        pncModel.date = "${fieldTanggal.text.toString()}T07:00:00+07:00"
        pncModel.subjective = fieldKeluhan.text.toString()
        pncModel.sistolik = fieldSistolik.text.toString()
        pncModel.diastolik = fieldDiastolik.text.toString()
        pncModel.nadi = fieldNadi.text.toString()
        pncModel.rr = fieldRr.text.toString()
        pncModel.suhu = fieldSuhu.text.toString()
        pncModel.bab = fieldBab.text.toString()
        pncModel.bak = fieldBak.text.toString()
        pncModel.kontaksi = fieldKontraksi.text.toString()
        pncModel.warna_lokhia = fieldWarna.text.toString()
        pncModel.jumlah_lokhia = fieldJumlahLokhia.text.toString()
        pncModel.produksi_asi = fieldAsi.text.toString()
        pncModel.komplikasi_kehamilan = fieldKomplikasi.text.toString()
        pncModel.terapi = fieldTerapi.text.toString()
        pncModel.nasihat = fieldNasihat.text.toString()
        pncModel.mother_id = dataMother.id
        pncModel.midwife_id = userPref.getUser().id
    }

    private fun isValid(): Boolean {
        if (fieldTanggal.text.toString().equals("")){
            fieldTanggal.setError("Wajib diisi")
            return false
        } else if (fieldKeluhan.text.toString().equals("")){
            fieldKeluhan.setError("Wajib diisi")
            return false
        } else if (fieldSistolik.text.toString().equals("")){
            fieldSistolik.setError("Wajib diisi")
            return false
        } else if (fieldDiastolik.text.toString().equals("")){
            fieldDiastolik.setError("Wajib diisi")
            return false
        } else if (fieldNadi.text.toString().equals("")){
            fieldNadi.setError("Wajib diisi")
            return false
        } else if (fieldRr.text.toString().equals("")){
            fieldRr.setError("Wajib diisi")
            return false
        } else if (fieldSuhu.text.toString().equals("")){
            fieldSuhu.setError("Wajib diisi")
            return false
        } else if (fieldBab.text.toString().equals("")){
            fieldBab.setError("Wajib diisi")
            return false
        } else if (fieldBak.text.toString().equals("")){
            fieldBak.setError("Wajib diisi")
            return false
        } else if (fieldKontraksi.text.toString().equals("")){
            fieldKontraksi.setError("Wajib diisi")
            return false
        } else if (fieldWarna.text.toString().equals("")){
            fieldWarna.setError("Wajib diisi")
            return false
        } else if (fieldJumlahLokhia.text.toString().equals("")){
            fieldJumlahLokhia.setError("Wajib diisi")
            return false
        } else if (fieldAsi.text.toString().equals("")){
            fieldAsi.setError("Wajib diisi")
            return false
        } else if (fieldKomplikasi.text.toString().equals("")){
            fieldAsi.setError("Wajib diisi")
            return false
        } else if (fieldTerapi.text.toString().equals("")){
            fieldTerapi.setError("Wajib diisi")
            return false
        } else if (fieldNasihat.text.toString().equals("")){
            fieldNasihat.setError("Wajib diisi")
            return false
        }
        return true
    }

    private fun callCalender() {
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
                    fieldTanggal.setText(tanggal)
                }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.minDate = Date().time
        datePickerDialog.show()
    }
}