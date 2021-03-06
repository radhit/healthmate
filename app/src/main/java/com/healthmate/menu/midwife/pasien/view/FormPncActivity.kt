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
import com.healthmate.common.functions.replaceEmpty
import com.healthmate.menu.midwife.pasien.data.PncModel
import com.healthmate.menu.reusable.data.Location
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
            removeError()
            if (isValid()){
                setDataInput()
                submit()
            }
        }
        fieldTanggal.setOnClickListener {
            callCalender()
        }
        fieldKeadaanIbu.setOnClickListener {
            navigator.dataMaster(this,"keadaan",1)
        }
        fieldKeadaanBayi.setOnClickListener {
            navigator.dataMaster(this,"keadaan",2)
        }
        fieldTfu.setOnClickListener {
            navigator.dataMaster(this, "tfu",3)
        }
        fieldKontraksi.setOnClickListener {
            navigator.dataMaster(this,"kontraksi",4)
        }
    }

    private fun removeError() {
        inputTanggal.setError(null)
        inputKeluhan.setError(null)
        inputTd.setError(null)
        inputNadi.setError(null)
        inputRR.setError(null)
        inputSuhu.setError(null)
        inputbab.setError(null)
        inputBak.setError(null)
        inputkontraksi.setError(null)
        inputWarna.setError(null)
        inputjumlahlokhia.setError(null)
        inputAsi.setError(null)
        inputKomplikasi.setError(null)
        inputTindakan.setError(null)
        inputNasihat.setError(null)
        inputPendarahan.setError(null)
        inputTfu.setError(null)
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
        pncModel.keluhan = fieldKeluhan.text.toString()
        pncModel.td = fieldSistolik.text.toString().toInt()
        pncModel.nadi = fieldNadi.text.toString().toInt()
        pncModel.rr = fieldRr.text.toString().toInt()
        pncModel.suhu = fieldSuhu.text.toString().toInt()
        pncModel.kontaksi = fieldKontraksi.text.toString()
        pncModel.tfu = fieldTfu.text.toString()
        pncModel.pendarahan = fieldPendarahan.text.toString().replaceEmpty("0").toInt()
        pncModel.warna_lokhia = fieldWarna.text.toString()
        pncModel.jumlah_lokhia = fieldJumlahLokhia.text.toString().replaceEmpty("0").toInt()
        pncModel.bab = fieldBab.text.toString()
        pncModel.bak = fieldBak.text.toString()
        pncModel.produksi_asi = fieldAsi.text.toString()
        pncModel.tindakan = fieldTindakan.text.toString()
        pncModel.nasihat = fieldNasihat.text.toString()
        pncModel.komplikasi_kehamilan = fieldKomplikasi.text.toString()
        pncModel.keadaan_ibu = fieldKeadaanIbu.text.toString()
        pncModel.keadaan_bayi = fieldKeadaanBayi.text.toString()
        pncModel.mother_id = dataMother.id
        pncModel.midwife_id = userPref.getUser().id
    }

    private fun isValid(): Boolean {
        if (fieldTanggal.text.toString().equals("")){
            inputTanggal.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldKeluhan.text.toString().equals("")){
            inputKeluhan.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldSistolik.text.toString().equals("")){
            inputTd.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldNadi.text.toString().equals("")){
            inputNadi.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldRr.text.toString().equals("")){
            inputRR.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldSuhu.text.toString().equals("")){
            inputSuhu.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldBab.text.toString().equals("")){
            inputbab.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldBak.text.toString().equals("")){
            inputBak.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldKontraksi.text.toString().equals("")){
            inputkontraksi.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldWarna.text.toString().equals("")){
            inputWarna.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldJumlahLokhia.text.toString().equals("")){
            inputjumlahlokhia.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldAsi.text.toString().equals("")){
            inputAsi.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldKomplikasi.text.toString().equals("")){
            inputKomplikasi.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldTindakan.text.toString().equals("")){
            inputTindakan.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldNasihat.text.toString().equals("")){
            inputNasihat.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldPendarahan.text.toString().equals("")){
            inputPendarahan.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldTfu.text.toString().equals("")){
            inputTfu.setError("Wajib diisi")
            Toast.makeText(this@FormPncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1){
            if (resultCode== Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), Location::class.java)
                fieldKeadaanIbu.setText(dataMaster.name)
            }
        } else if (requestCode==2){
            if (resultCode== Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), Location::class.java)
                fieldKeadaanBayi.setText(dataMaster.name)
            }
        } else if (requestCode==3){
            if (resultCode== Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), Location::class.java)
                fieldTfu.setText(dataMaster.name)
            }
        } else if (requestCode==4){
            if (resultCode== Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), Location::class.java)
                fieldKontraksi.setText(dataMaster.name)
            }
        }
    }
}