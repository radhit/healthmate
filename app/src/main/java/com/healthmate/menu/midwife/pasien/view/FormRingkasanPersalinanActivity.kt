package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.healthmate.R
import com.healthmate.api.DataResponse
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.menu.midwife.pasien.data.Summary
import kotlinx.android.synthetic.main.activity_form_ringkasan_persalinan.*
import kotlinx.android.synthetic.main.activity_form_ringkasan_persalinan.btn_simpan
import kotlinx.android.synthetic.main.activity_form_ringkasan_persalinan.fieldWaktu
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FormRingkasanPersalinanActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String): Intent {
            val intent = Intent(activity, FormRingkasanPersalinanActivity::class.java)
            intent.putExtra(EXTRA,data)
            return intent
        }
    }

    override fun getView(): Int = R.layout.activity_form_ringkasan_persalinan
    var summary: Summary = Summary()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Formulir Ringkasan Persalinan")
        summary = gson.fromJson(intent.getStringExtra(EXTRA), Summary::class.java)
        if (!summary.date_birth.equals("")){
            setData()
        }
        btn_simpan.setOnClickListener {
            if (isValid()){
                setDataSubmit()
                submit()
            }
        }
        fieldTanggalPersalinan.setOnClickListener {
            callCalender()
        }
        fieldWaktu.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showTimePicker() {//jam_buka & jam_tutup
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener { _, i, i1 ->
                    var waktu = ""
                    if (i < 10) {
                        waktu = "0" + i.toString() + ":"
                    } else {
                        waktu = i.toString() + ":"
                    }
                    if (i1 < 10) {
                        waktu = waktu + "0" + i1.toString()
                    } else {
                        waktu = waktu + i1.toString()
                    }
                    waktu="${waktu}:00"
                    fieldWaktu.setText(waktu)

                }, hour, minute, true)
        timePickerDialog.show()
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
                    fieldTanggalPersalinan.setText(tanggal)
                }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.minDate = Date().time
        datePickerDialog.show()
    }

    private fun submit() {
        showLoadingDialog()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(summary))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.updateForm("${Urls.inc}/${summary.inc_id}/summary",requestBody)
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
                    Toast.makeText(this@FormRingkasanPersalinanActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormRingkasanPersalinanActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setDataSubmit() {
        summary.date_birth = "${fieldTanggalPersalinan.text.toString()}T${fieldWaktu.text.toString()}+07:00"
        summary.time_birth = fieldWaktu.text.toString()
        summary.age_pregnancy = fieldUsiaKehamilan.text.toString()
        summary.birth_attendant = fieldPenolong.text.toString()
        summary.mode_delivery = fieldCara.text.toString()
        summary.mother_condition = fieldKeadaanIbu.text.toString()
        summary.information = fieldKeterangan.text.toString()
    }

    private fun isValid(): Boolean {
        if (fieldTanggalPersalinan.text.toString().equals("")){
            fieldTanggalPersalinan.setError("Wajib diisi")
            return false
        } else if (fieldWaktu.text.toString().equals("")){
            fieldWaktu.setError("Wajib diisi")
            return false
        } else if (fieldUsiaKehamilan.text.toString().equals("")){
            fieldUsiaKehamilan.setError("Wajib diisi")
            return false
        } else if (fieldPenolong.text.toString().equals("")){
            fieldPenolong.setError("Wajib diisi")
            return false
        } else if (fieldCara.text.toString().equals("")){
            fieldCara.setError("Wajib diisi")
            return false
        } else if (fieldKeadaanIbu.text.toString().equals("")){
            fieldKeadaanIbu.setError("Wajib diisi")
            return false
        } else if (fieldKeterangan.text.toString().equals("")){
            fieldKeterangan.setError("Wajib diisi")
            return false
        }
        return true
    }

    private fun setData() {
        fieldTanggalPersalinan.setText("${summary.date_birth!!.split("T")[0]}")
        fieldWaktu.setText("${summary.time_birth!!}")
        fieldUsiaKehamilan.setText("${summary.age_pregnancy!!}")
        fieldPenolong.setText("${summary.birth_attendant!!}")
        fieldCara.setText("${summary.mode_delivery!!}")
        fieldKeadaanIbu.setText("${summary.mother_condition!!}")
        fieldKeterangan.setText("${summary.information!!}")
    }
}