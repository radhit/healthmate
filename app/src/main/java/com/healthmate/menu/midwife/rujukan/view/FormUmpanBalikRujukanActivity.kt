package com.healthmate.menu.midwife.rujukan.view

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.healthmate.R
import com.healthmate.api.DataResponse
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.menu.midwife.rujukan.data.Midwife
import com.healthmate.menu.midwife.rujukan.data.Rujukan
import com.healthmate.menu.midwife.rujukan.data.UmpanbalikRujukan
import com.healthmate.menu.reusable.data.Hospital
import kotlinx.android.synthetic.main.activity_form_input_anc.*
import kotlinx.android.synthetic.main.activity_form_rujukan.*
import kotlinx.android.synthetic.main.activity_form_umpan_balik_rujukan.*
import kotlinx.android.synthetic.main.activity_form_umpan_balik_rujukan.btn_simpan
import kotlinx.android.synthetic.main.activity_form_umpan_balik_rujukan.fieldAsal
import kotlinx.android.synthetic.main.activity_form_umpan_balik_rujukan.fieldDiagnosis
import kotlinx.android.synthetic.main.activity_form_umpan_balik_rujukan.fieldTanggal
import kotlinx.android.synthetic.main.activity_form_umpan_balik_rujukan.fieldWaktu
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FormUmpanBalikRujukanActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String): Intent {
            val intent = Intent(activity, FormUmpanBalikRujukanActivity::class.java)
            intent.putExtra(EXTRA,data)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_form_umpan_balik_rujukan
    var rujukan: Rujukan = Rujukan()
    var umpanBalikRujukan: UmpanbalikRujukan = UmpanbalikRujukan()
    var asal: Hospital = Hospital()
    var midwife: Midwife = Midwife()

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        rujukan = gson.fromJson(intent.getStringExtra(EXTRA),Rujukan::class.java)
        umpanBalikRujukan = rujukan.umpanbalik_rujukan!!
        if (!umpanBalikRujukan.datetime.equals("")){
            setData()
        } else{
            midwife.name = userPref.getUser().name
            midwife.id = userPref.getUser().id
            asal = userPref.getUser().hospital!!
            fieldPenerimaRujukan.setText("${midwife.name}")
            fieldAsal.setText("${asal.name}")
        }
        fieldTanggal.setOnClickListener {
            callCalender()
        }
        fieldWaktu.setOnClickListener {
            showTimePicker()
        }
        fieldAsal.setOnClickListener {
            navigator.listLocation(this,"rujukan",1)
        }
        btn_simpan.setOnClickListener {
            if (isValid()){
                setDataInput()
                submit()
            }
        }
    }

    private fun setData() {
        fieldTanggal.setText("${umpanBalikRujukan.datetime.split("T")[0]}")
        fieldWaktu.setText("${umpanBalikRujukan.datetime.split("T")[0].split("+")[0]}")
        fieldPenerimaRujukan.setText("${umpanBalikRujukan.penerima.name}")
        midwife = umpanBalikRujukan.penerima
        asal = umpanBalikRujukan.asal_penerima
        fieldAsal.setText("${umpanBalikRujukan.asal_penerima.name}")
        fieldDiagnosis.setText("${umpanBalikRujukan.diagnosis}")
        fieldAnjuran.setText("${umpanBalikRujukan.anjuran}")
        btn_simpan.isEnabled = false
        btn_simpan.background = ContextCompat.getDrawable(this, R.drawable.bg_rounded_grey)
        btn_simpan.setTextColor(resources.getColor(R.color.white))
    }

    private fun submit() {
        showLoadingDialog()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(rujukan))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.updateForm("${Urls.rujukan}/${rujukan.id}",requestBody)
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
                    Toast.makeText(this@FormUmpanBalikRujukanActivity,"Got error. Please try again later.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormUmpanBalikRujukanActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setDataInput() {
        umpanBalikRujukan.datetime = "${fieldTanggal.text.toString()}T${fieldWaktu.text.toString()}+07:00"
        midwife.name = fieldPenerimaRujukan.text.toString()

        umpanBalikRujukan.penerima = midwife
        umpanBalikRujukan.asal_penerima = asal
        umpanBalikRujukan.diagnosis = fieldDiagnosis.text.toString()
        umpanBalikRujukan.anjuran = fieldAnjuran.text.toString()
        rujukan.umpanbalik_rujukan = umpanBalikRujukan
    }

    private fun isValid(): Boolean {
        if (fieldTanggal.text.toString().equals("")){
            fieldTanggal.setError("Wajib diisi")
            Toast.makeText(this@FormUmpanBalikRujukanActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldWaktu.text.toString().equals("")){
            fieldWaktu.setError("Wajib diisi")
            Toast.makeText(this@FormUmpanBalikRujukanActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldPenerimaRujukan.text.toString().equals("")){
            fieldPenerimaRujukan.setError("Wajib diisi")
            Toast.makeText(this@FormUmpanBalikRujukanActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldAsal.text.toString().equals("")){
            fieldAsal.setError("Wajib diisi")
            Toast.makeText(this@FormUmpanBalikRujukanActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldAnjuran.text.toString().equals("")){
            fieldAnjuran.setError("Wajib diisi")
            Toast.makeText(this@FormUmpanBalikRujukanActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldDiagnosis.text.toString().equals("")){
            fieldDiagnosis.setError("Wajib diisi")
            Toast.makeText(this@FormUmpanBalikRujukanActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
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
                R.style.MySpinnerDatePickerStyle,
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
                fieldRujukan.setText(dataMaster.name)
                asal = dataMaster
                fieldAsal.setText("${asal.name}")
            }
        }
    }
}