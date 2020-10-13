package com.healthmate.menu.midwife.rujukan.view

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
import com.healthmate.menu.midwife.rujukan.data.Midwife
import com.healthmate.menu.midwife.rujukan.data.Rujukan
import com.healthmate.menu.reusable.data.Hospital
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_form_rujukan.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FormRujukanActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        val EXTRA_KETERANGAN = "EXTRA_KETERANGAN"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String, keterangan: String = ""): Intent {
            val intent = Intent(activity, FormRujukanActivity::class.java)
            intent.putExtra(EXTRA,data)
            intent.putExtra(EXTRA_KETERANGAN,keterangan)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_form_rujukan
    var rujukanModel: Rujukan = Rujukan()
    var user: User = User()
    var midwife: Midwife = Midwife()
    var asal: Hospital = Hospital()
    var tujuan: Hospital = Hospital()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        println(" data rujukan ${intent.getStringExtra(EXTRA)}")
        if (intent.getStringExtra(EXTRA_KETERANGAN).equals("create")){
            rujukanModel.mother = gson.fromJson(intent.getStringExtra(EXTRA),User::class.java)
            setInputEnable()
        } else{
            rujukanModel = gson.fromJson(intent.getStringExtra(EXTRA),Rujukan::class.java)
            setInputDisable()
        }
        fieldNama.setText("${userPref.getUser().name}")
        fieldAsal.setText("${userPref.getUser().hospital!!.name}")
        asal = userPref.getUser().hospital!!
        fieldTanggal.setOnClickListener {
            callCalender()
        }
        fieldWaktu.setOnClickListener {
            showTimePicker()
        }
        fieldAsal.setOnClickListener {
            navigator.listLocation(this, "rujukan",1)
        }
        fieldTujuan.setOnClickListener {
            navigator.listLocation(this,"rujukan",2)
        }
        btn_simpan.setOnClickListener {
            if (isValid()){
                setDataInput()
                submit()
            }
        }
    }

    private fun submit() {
        showLoadingDialog()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(rujukanModel))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.inputForm("${Urls.rujukan}",requestBody)
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
                    Toast.makeText(this@FormRujukanActivity,"Got error. Please try again later.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormRujukanActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setInputEnable() {

    }

    private fun setInputDisable() {

    }

    private fun setDataInput() {
        rujukanModel.datetime = "${fieldTanggal.text.toString()}T${fieldWaktu.text.toString()}+07:00"
        midwife.id = userPref.getUser().id
        midwife.name = fieldNama.text.toString()

        rujukanModel.perujuk = midwife
        rujukanModel.asal_perujuk = asal
        rujukanModel.sebab = fieldSebab.text.toString()
        rujukanModel.diagnosis = fieldDiagnosis.text.toString()
        rujukanModel.tindakan = fieldTindakan.text.toString()
        rujukanModel.tujuan = tujuan
    }

    private fun isValid(): Boolean {
        if (fieldTanggal.text.toString().equals("")){
            fieldTanggal.setError("Wajib diisi")
            return false
        } else if (fieldWaktu.text.toString().equals("")){
            fieldWaktu.setError("Wajib diisi")
            return false
        } else if (fieldNama.text.toString().equals("")){
            fieldNama.setError("Wajib diisi")
            return false
        } else if (fieldSebab.text.toString().equals("")){
            fieldSebab.setError("Wajib diisi")
            return false
        } else if (fieldDiagnosis.text.toString().equals("")){
            fieldDiagnosis.setError("Wajib diisi")
            return false
        } else if (fieldTindakan.text.toString().equals("")){
            fieldTindakan.setError("Wajib diisi")
            return false
        } else if (fieldAsal.text.toString().equals("")){
            fieldAsal.setError("Wajib diisi")
            return false
        } else if (fieldTujuan.text.toString().equals("")){
            fieldTujuan.setError("Wajib diisi")
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
                android.R.style.Theme_Holo_Light_Dialog,
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
        datePickerDialog.show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),Hospital::class.java)
                asal = dataMaster
                fieldAsal.setText("${asal.name}")
            }
        } else if (requestCode==2){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), Hospital::class.java)
                tujuan = dataMaster
                fieldTujuan.setText(dataMaster.name)
            }
        }
    }
}