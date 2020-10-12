package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.healthmate.R
import com.healthmate.api.DataResponse
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.menu.midwife.pasien.data.IncKalaModel
import com.healthmate.menu.reusable.data.MasterListModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_form_input_inc.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class FormInputKalaActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        val EXTRA_TYPE = "EXTRA_TYPE"
        @JvmStatic
        fun getCallingIntent(activity: Activity, type: String, data: String): Intent {
            val intent = Intent(activity, FormInputKalaActivity::class.java)
            intent.putExtra(EXTRA_TYPE, type)
            intent.putExtra(EXTRA, data)
            return intent
        }
    }

    override fun getView(): Int = R.layout.activity_form_input_inc
    var dataMother: User = User()
    var type: String = ""
    var incKalaModel: IncKalaModel = IncKalaModel()
    var dateNow: String = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Formulir Kala 1")
        var current = LocalDateTime.now().toString()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        dateNow = current.format(formatter)
        dataMother = gson.fromJson(intent.getStringExtra(EXTRA),User::class.java)
        type = intent.getStringExtra(EXTRA_TYPE)
        btn_simpan.setOnClickListener {
            if (isValid()){
                setDataInput()
                submit()
            }
        }
        rb_kembar.setOnClickListener {
            rb_kembar.isChecked = true
            rb_tunggal.isChecked = false
            inputDjj2.visibility = View.VISIBLE
        }
        rb_tunggal.setOnClickListener {
            rb_kembar.isChecked = false
            rb_tunggal.isChecked = true
            inputDjj2.visibility = View.GONE
        }
        fieldTanggal.setOnClickListener {
            callCalender()
        }
        fieldWaktu.setOnClickListener {
            showTimePicker()
        }
        fieldKesadaran.setOnClickListener {
            navigator.dataMaster(this,"kesadaran",1)
        }
    }

    private fun submit() {
        showLoadingDialog()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(incKalaModel))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.inputForm("${Urls.kala}",requestBody)
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
                    Toast.makeText(this@FormInputKalaActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormInputKalaActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setDataInput() {
        incKalaModel.datetime = "${fieldTanggal.text.toString()}T${fieldWaktu.text.toString()}+07:00"
//        incKalaModel.time = fieldWaktu.text.toString()
        incKalaModel.complaint = fieldKeluhan.text.toString()
        incKalaModel.awareness = fieldKesadaran.text.toString()
        incKalaModel.td = fieldTd.text.toString().toInt()
        incKalaModel.tfu = fieldTfu.text.toString().toInt()
        incKalaModel.his = fieldHis.text.toString().toInt()
        if (rb_kembar.isChecked){
            incKalaModel.djj.add(fieldDjj.text.toString().toInt())
            incKalaModel.djj.add(fieldDjj2.text.toString().toInt())
        } else{
            incKalaModel.djj.add(fieldDjj.text.toString().toInt())
        }
        incKalaModel.pulse = fieldNadi.text.toString().toInt()
        incKalaModel.amniotic_fluid = fieldKetuban.text.toString()
        incKalaModel.rr = fieldRr.text.toString().toInt()
        incKalaModel.temperature = fieldSuhu.text.toString().toInt()
        incKalaModel.effacement = fieldEffacement.text.toString().toInt()
        incKalaModel.analisys = fieldAnalisis.text.toString()
        incKalaModel.penatalaksaan = fieldPenatalaksaan.text.toString()
        incKalaModel.vt = fieldVT.text.toString().toInt()
        incKalaModel.type = type
        incKalaModel.mother_id = dataMother.id
        incKalaModel.midwife_id = userPref.getUser().id
    }

    private fun isValid(): Boolean {
        if (fieldTanggal.text.toString().equals("")){
            fieldTanggal.setError("Wajib diisi")
            return false
        } else if (fieldWaktu.text.toString().equals("")){
            fieldWaktu.setError("Wajib diisi")
            return false
        } else if (fieldKeluhan.text.toString().equals("")){
            fieldKeluhan.setError("Wajib diisi")
            return false
        } else if (fieldTd.text.toString().equals("")){
            fieldTd.setError("Wajib diisi")
            return false
        } else if (fieldTfu.text.toString().equals("")){
            fieldTfu.setError("Wajib diisi")
            return false
        } else if (fieldHis.text.toString().equals("")){
            fieldHis.setError("Wajib diisi")
            return false
        } else if (fieldDjj.text.toString().equals("")){
            fieldDjj.setError("Wajib diisi")
            return false
        } else if (fieldNadi.text.toString().equals("")){
            fieldNadi.setError("Wajib diisi")
            return false
        } else if (fieldKetuban.text.toString().equals("")){
            fieldKetuban.setError("Wajib diisi")
            return false
        } else if (fieldRr.text.toString().equals("")){
            fieldRr.setError("Wajib diisi")
            return false
        } else if (fieldVT.text.toString().equals("")){
            fieldVT.setError("Wajib diisi")
            return false
        } else if (fieldSuhu.text.toString().equals("")){
            fieldSuhu.setError("Wajib diisi")
            return false
        } else if (fieldEffacement.text.toString().equals("")){
            fieldEffacement.setError("Wajib diisi")
            return false
        } else if (fieldAnalisis.text.toString().equals("")){
            fieldAnalisis.setError("Wajib diisi")
            return false
        } else if (fieldPenatalaksaan.text.toString().equals("")){
            fieldPenatalaksaan.setError("Wajib diisi")
            return false
        } else if (fieldKesadaran.text.toString().equals("")){
            fieldKesadaran.setError("Wajib diisi")
            return false
        }
        if (rb_kembar.isChecked){
            if (fieldDjj2.text.toString().equals("")){
                fieldDjj2.setError("Wajib diisi")
                return false
            }
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
        datePickerDialog.datePicker.minDate = Date().time
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
        if (resultCode== RESULT_OK){
            if (requestCode==1){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldKesadaran.setText(dataMaster.name)
            }
        }
    }
}