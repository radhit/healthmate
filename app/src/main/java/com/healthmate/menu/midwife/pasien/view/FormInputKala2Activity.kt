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
import kotlinx.android.synthetic.main.activity_form_input_kala2.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class FormInputKala2Activity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        val EXTRA_TYPE = "EXTRA_TYPE"
        @JvmStatic
        fun getCallingIntent(activity: Activity, type: String, data: String): Intent {
            val intent = Intent(activity, FormInputKala2Activity::class.java)
            intent.putExtra(EXTRA_TYPE, type)
            intent.putExtra(EXTRA, data)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_form_input_kala2
    var dataMother: User = User()
    var type: String = ""
    var incKalaModel: IncKalaModel = IncKalaModel()
    var dateNow: String = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Formulir Kala 2")
        var current = LocalDateTime.now().toString()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        dateNow = current.format(formatter)
        dataMother = gson.fromJson(intent.getStringExtra(FormInputKalaActivity.EXTRA),User::class.java)
        type = intent.getStringExtra(FormInputKalaActivity.EXTRA_TYPE)
        btn_simpan.setOnClickListener {
            removeError()
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
        fieldPresentasi.setOnClickListener {
            navigator.dataMaster(this,"presentasi",2)
        }
        fieldDenominator.setOnClickListener {
            navigator.dataMaster(this,"denominator",3)
        }
        fieldTaliPusar.setOnClickListener {
            navigator.dataMaster(this,"tali pusar",4)
        }
        fieldPernium.setOnClickListener {
            navigator.dataMaster(this,"pernium",5)
        }
        fieldVulva.setOnClickListener {
            navigator.dataMaster(this,"vulva vagina",6)
        }
    }

    private fun removeError() {
        inputTanggal.setError(null)
        inputWaktu.setError(null)
        inputKeluhan.setError(null)
        inputTd.setError(null)
        inputHis.setError(null)
        inputDjj1.setError(null)
        inputNadi.setError(null)
        inputKetuban.setError(null)
        inputRR.setError(null)
        inputVt.setError(null)
        inputSuhu.setError(null)
        inputEffacement.setError(null)
        inputAnalisis.setError(null)
        inputPenatalaksaan.setError(null)
        inputKesadaran.setError(null)
        inputTaliPusar.setError(null)
        inputPresentasi.setError(null)
        inputDenominator.setError(null)
        inputPernium.setError(null)
        inputVulva.setError(null)
        inputDjj2.setError(null)

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
                    Toast.makeText(this@FormInputKala2Activity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormInputKala2Activity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setDataInput() {
        incKalaModel.datetime = "${fieldTanggal.text.toString()}T${fieldWaktu.text.toString()}+07:00"
//        incKalaModel.time = fieldWaktu.text.toString()
        incKalaModel.complaint = fieldKeluhan.text.toString()
        incKalaModel.awareness = fieldKesadaran.text.toString()
        incKalaModel.td = fieldTd.text.toString().toInt()
        incKalaModel.pulse = fieldNadi.text.toString().toInt()
        incKalaModel.rr = fieldRr.text.toString().toInt()
        incKalaModel.temperature = fieldSuhu.text.toString().toInt()

        if (rb_kembar.isChecked){
            incKalaModel.djj.add(fieldDjj.text.toString().toInt())
            incKalaModel.djj.add(fieldDjj2.text.toString().toInt())
        } else{
            incKalaModel.djj.add(fieldDjj.text.toString().toInt())
        }
        incKalaModel.his = fieldHis.text.toString().toInt()
        incKalaModel.amniotic_fluid = fieldKetuban.text.toString()
        incKalaModel.vt = fieldVT.text.toString().toInt()
        incKalaModel.effacement = fieldEffacement.text.toString().toInt()
        incKalaModel.warna_ketuban = fieldWarnaKetuban.text.toString()
        incKalaModel.presentasi = fieldPresentasi.text.toString()
        incKalaModel.denominator = fieldDenominator.text.toString()
        incKalaModel.tali_pusar = fieldTaliPusar.text.toString()
        incKalaModel.pernium_menonjol = fieldPernium.text.toString()
        incKalaModel.vulva_vagina = fieldVulva.text.toString()

        incKalaModel.analisys = fieldAnalisis.text.toString()
        incKalaModel.penatalaksaan = fieldPenatalaksaan.text.toString()
        incKalaModel.type = type
        incKalaModel.mother_id = dataMother.id
        incKalaModel.midwife_id = userPref.getUser().id
    }

    private fun isValid(): Boolean {
        if (fieldTanggal.text.toString().equals("")){
            inputTanggal.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldWaktu.text.toString().equals("")){
            inputWaktu.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldKeluhan.text.toString().equals("")){
            inputKeluhan.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldTd.text.toString().equals("")){
            inputTd.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldHis.text.toString().equals("")){
            inputHis.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldDjj.text.toString().equals("")){
            inputDjj1.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldNadi.text.toString().equals("")){
            inputNadi.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldKetuban.text.toString().equals("")){
            inputKetuban.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldRr.text.toString().equals("")){
            inputRR.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldVT.text.toString().equals("")){
            inputVt.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldSuhu.text.toString().equals("")){
            inputSuhu.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldEffacement.text.toString().equals("")){
            inputEffacement.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldAnalisis.text.toString().equals("")){
            inputAnalisis.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldPenatalaksaan.text.toString().equals("")){
            inputPenatalaksaan.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldKesadaran.text.toString().equals("")){
            inputKesadaran.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldTaliPusar.text.toString().equals("")){
            inputTaliPusar.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldPresentasi.text.toString().equals("")){
            inputPresentasi.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldDenominator.text.toString().equals("")){
            inputDenominator.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldPernium.text.toString().equals("")){
            inputPernium.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldVulva.text.toString().equals("")){
            inputVulva.setError("Wajib diisi")
            Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        }
        if (rb_kembar.isChecked){
            if (fieldDjj2.text.toString().equals("")){
                inputDjj2.setError("Wajib diisi")
                Toast.makeText(this@FormInputKala2Activity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
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
        if (resultCode== RESULT_OK){
            if (requestCode==1){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldKesadaran.setText(dataMaster.name)
            } else if (requestCode==2){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldPresentasi.setText(dataMaster.name)
            } else if (requestCode==3){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldDenominator.setText(dataMaster.name)
            } else if (requestCode==4){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldTaliPusar.setText(dataMaster.name)
            } else if (requestCode==5){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldPernium.setText(dataMaster.name)
            } else if (requestCode==6){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldVulva.setText(dataMaster.name)
            }
        }
    }
}