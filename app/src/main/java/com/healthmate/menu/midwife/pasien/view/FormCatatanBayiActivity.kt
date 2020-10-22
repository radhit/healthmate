package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.healthmate.R
import com.healthmate.api.DataResponse
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.replaceEmpty
import com.healthmate.menu.midwife.pasien.data.BabyNote
import com.healthmate.menu.reusable.data.MasterListModel
import kotlinx.android.synthetic.main.activity_form_catatan_bayi.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormCatatanBayiActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String): Intent {
            val intent = Intent(activity, FormCatatanBayiActivity::class.java)
            intent.putExtra(EXTRA,data)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_form_catatan_bayi
    var babyNote: BabyNote = BabyNote()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Formulir Catatan Bayi")
        babyNote = gson.fromJson(intent.getStringExtra(EXTRA),BabyNote::class.java)
        if (!babyNote.gender.equals("")){
            setData()
        }
        btn_simpan.setOnClickListener {
            removeError()
            if (isValid()){
                setDataInput()
                submit()
            }
        }
        fieldKeadaanLahir.setOnClickListener {
            navigator.dataMaster(this, "keadaan",1)
        }
    }

    private fun removeError() {
        inputAnakKe.setError(null)
        inputBerat.setError(null)
        inputPanjang.setError(null)
        inputLingkar.setError(null)
        inputJenisKelamin.setError(null)
        inputKeadaanLahir.setError(null)
        inputSkor.setError(null)
        inputKeterangan.setError(null)

    }

    private fun submit() {
        showLoadingDialog()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(babyNote))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.updateForm("${Urls.inc}/${babyNote.inc_id}/baby-note",requestBody)
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
                    Toast.makeText(this@FormCatatanBayiActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormCatatanBayiActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setDataInput() {
        babyNote.child_number = fieldAnakKe.text.toString().replaceEmpty("0").toInt()
        babyNote.weight = fieldBerat.text.toString().replaceEmpty("0").toInt()
        babyNote.height = fieldPanjang.text.toString().replaceEmpty("0").toInt()
        babyNote.lila = fieldLingkar.text.toString().replaceEmpty("0").toInt()
        babyNote.gender = fieldJenisKelamin.text.toString()
        babyNote.baby_condition = fieldKeadaanLahir.text.toString()
        babyNote.apgar = fieldSkor.text.toString().replaceEmpty("0").toInt()
        babyNote.information = fieldKeterangan.text.toString()
        var asuhan: ArrayList<String> = arrayListOf()
        if (cb_1.isChecked){
            asuhan.add("IMD 1 Jam Pertama")
        }
        if (cb_2.isChecked){
            asuhan.add("Suntik Vit.K1")
        }
        if (cb_3.isChecked){
            asuhan.add("Salep Mata Antibiotik")
        }
        if (cb_4.isChecked){
            asuhan.add("Imunisasi HB0")
        }
        babyNote.asuhan_bayi = asuhan
    }

    private fun isValid(): Boolean {
        if (fieldAnakKe.text.toString().equals("")){
            inputAnakKe.setError("Wajib diisi")
            return false
        } else if (fieldBerat.text.toString().equals("")){
            inputBerat.setError("Wajib diisi")
            return false
        } else if (fieldPanjang.text.toString().equals("")){
            inputPanjang.setError("Wajib diisi")
            return false
        } else if (fieldLingkar.text.toString().equals("")){
            inputLingkar.setError("Wajib diisi")
            return false
        } else if (fieldJenisKelamin.text.toString().equals("")){
            inputJenisKelamin.setError("Wajib diisi")
            return false
        } else if (fieldKeadaanLahir.text.toString().equals("")){
            inputKeadaanLahir.setError("Wajib diisi")
            return false
        } else if (fieldSkor.text.toString().equals("")){
            inputSkor.setError("Wajib diisi")
            return false
        }  else if (fieldKeterangan.text.toString().equals("")){
            inputKeterangan.setError("Wajib diisi")
            return false
        }
        return true
    }

    private fun setData() {
        fieldAnakKe.setText("${babyNote.child_number}")
        fieldBerat.setText("${babyNote.weight}")
        fieldPanjang.setText("${babyNote.height}")
        fieldLingkar.setText("${babyNote.lila}")
        fieldJenisKelamin.setText("${babyNote.gender}")
        fieldKeadaanLahir.setText("${babyNote.baby_condition}")
        fieldSkor.setText("${babyNote.apgar}")
        if (babyNote.asuhan_bayi!!.size>0){
            for (i in 0..babyNote.asuhan_bayi!!.size-1){
                if (babyNote.asuhan_bayi!![i].contains("IMD")){
                    cb_1.isChecked = true
                } else if (babyNote.asuhan_bayi!![i].contains("Suntik")){
                    cb_2.isChecked = true
                } else if (babyNote.asuhan_bayi!![i].contains("Salep")){
                    cb_3.isChecked = true
                } else if (babyNote.asuhan_bayi!![i].contains("Imunisasi")){
                    cb_4.isChecked = true
                }
            }
        }
        fieldKeterangan.setText("${babyNote.information}")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK){
            if (requestCode==1){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldKeadaanLahir.setText(dataMaster.name)
            }
        }
    }
}