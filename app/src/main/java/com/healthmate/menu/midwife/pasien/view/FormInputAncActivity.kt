package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.healthmate.R
import com.healthmate.api.DataResponse
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.menu.mom.rapor.data.AncModel
import com.healthmate.menu.reusable.data.Hospital
import com.healthmate.menu.reusable.data.Location
import com.healthmate.menu.reusable.data.MasterListModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_form_input_anc.*
import kotlinx.android.synthetic.main.activity_form_input_anc.btn_simpan
import kotlinx.android.synthetic.main.activity_form_input_anc.fieldDiastolik
import kotlinx.android.synthetic.main.activity_form_input_anc.fieldDjj
import kotlinx.android.synthetic.main.activity_form_input_anc.fieldDjj2
import kotlinx.android.synthetic.main.activity_form_input_anc.fieldNadi
import kotlinx.android.synthetic.main.activity_form_input_anc.fieldRr
import kotlinx.android.synthetic.main.activity_form_input_anc.fieldSistolik
import kotlinx.android.synthetic.main.activity_form_input_anc.fieldTanggal
import kotlinx.android.synthetic.main.activity_form_input_anc.fieldTfu
import kotlinx.android.synthetic.main.activity_form_input_anc.rb_kembar
import kotlinx.android.synthetic.main.activity_form_input_anc.rb_tunggal
import kotlinx.android.synthetic.main.activity_form_input_inc.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class FormInputAncActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String): Intent {
            val intent = Intent(activity, FormInputAncActivity::class.java)
            intent.putExtra(EXTRA, data)
            return intent
        }
    }

    override fun getView(): Int = R.layout.activity_form_input_anc
    var ancModel: AncModel = AncModel()
    var mother: User = User()
    var hospital: Hospital = Hospital()
    var dateNow: String = ""
    var dateComeback: String = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Formulir ANC")
        mother = gson.fromJson(intent.getStringExtra(EXTRA),User::class.java)
        ll_form.visibility = View.VISIBLE
        ll_hospital.visibility = View.GONE

        var current = LocalDateTime.now().toString()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        dateNow = current.format(formatter)
        println("date comeback : ${dateNow}")
        tv_tanggal.text = dateNow.split("T")[0]
        fieldGoldar.setOnClickListener {
            navigator.dataMaster(this,"goldar",1)
        }
        fieldRhesus.setOnClickListener {
            navigator.dataMaster(this, "rhesus",2)
        }
        fieldHepatitis.setOnClickListener {
            navigator.dataMaster(this,"hepatitis",3)
        }
        fieldBta.setOnClickListener {
            navigator.dataMaster(this,"bta",4)
        }
        fieldHiv.setOnClickListener {
            navigator.dataMaster(this,"hiv",5)
        }
        fieldMalaria.setOnClickListener {
            navigator.dataMaster(this,"malaria",6)
        }
        fieldSifilis.setOnClickListener {
            navigator.dataMaster(this,"sifilis",7)
        }
        fieldTanggal.setOnClickListener {
            callCalender()
        }

        btn_verif.setOnClickListener {
            if (isValid()){
                setData()
                submit()
            }
        }
        fieldRujukan.setOnClickListener {
            navigator.listLocation(this,"rujukan",8,gson.toJson(mother))
        }
        fieldKakiBengkak.setOnClickListener {
            navigator.dataMaster(this,"kaki bengkak",9)
        }
        fieldProteinUrin.setOnClickListener {
            navigator.dataMaster(this,"protein urin",10)
        }
        fieldReduksiUrin.setOnClickListener {
            navigator.dataMaster(this,"reduksi urin",11)
        }
        fieldLetakJanin.setOnClickListener {
            navigator.dataMaster(this,"letak janin",12)
        }
        rb_kembar.setOnClickListener {
            rb_kembar.isChecked = true
            rb_tunggal.isChecked = false
            fieldDjj2.visibility = View.VISIBLE
        }
        rb_tunggal.setOnClickListener {
            rb_kembar.isChecked = false
            rb_tunggal.isChecked = true
            fieldDjj2.visibility = View.GONE
        }
        btn_simpan.setOnClickListener {
            if (fieldRujukan.text.toString().equals("")){
                fieldRujukan.setError("Wajib diisi")
            } else{
                postAnc()
            }
        }
    }

    private fun postAnc() {
        showLoadingDialog()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(ancModel))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.inputForm("${Urls.ancsMom}",requestBody)
        call.enqueue(object : Callback<DataResponse<Any>> {
            override fun onResponse(call: Call<DataResponse<Any>>?, response: Response<DataResponse<Any>>?) {
                closeLoadingDialog()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299){
                        dialogFinish()
                    } else{
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@FormInputAncActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormInputAncActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun dialogFinish() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.item_dialog_post_anc_midwife)
        val tv_name = dialog.findViewById(R.id.tv_nama) as TextView
        tv_name.text = "Good Job\n${userPref.getUser().name}"
        val yesBtn = dialog.findViewById(R.id.btn_finish) as Button
        yesBtn.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

    private fun submit() {
        println("data anc ${gson.toJson(ancModel)}")
        showLoadingDialog()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(ancModel))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<AncModel>> = baseApi.validatorAnc("/ancs-validator",requestBody)
        call.enqueue(object : Callback<DataResponse<AncModel>> {
            override fun onResponse(call: Call<DataResponse<AncModel>>?, response: Response<DataResponse<AncModel>>?) {
                closeLoadingDialog()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299){
                        println("data validator ${gson.toJson(response.body()!!.data)}")
                        ancModel.diagnostic_color = response.body()!!.data!!.diagnostic_color
                        println("data anc ${gson.toJson(ancModel)}")
                        ll_form.visibility = View.GONE
                        ll_hospital.visibility = View.VISIBLE
                        if (ancModel.diagnostic_color.equals("red")){
                            Glide.with(this@FormInputAncActivity).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.status_red)).into(iv_status)
                        } else if (ancModel.diagnostic_color.equals("green")){
                            Glide.with(this@FormInputAncActivity).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.status_green)).into(iv_status)
                        } else{
                            Glide.with(this@FormInputAncActivity).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.status_yellow)).into(iv_status)
                        }
                    } else{
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@FormInputAncActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<AncModel>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormInputAncActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setData() {
        ancModel.mother_id = mother.id
        ancModel.midwife_id = userPref.getUser().id
        ancModel.date = "${dateNow}+07:00"
        ancModel.complaint = fieldKeluhanUtama.text.toString()
        ancModel.other_complaint = fieldKeluhanLainnya.text.toString()
        ancModel.sistolik = fieldSistolik.text.toString()
        ancModel.diastolik = fieldDiastolik.text.toString()
        ancModel.nadi = fieldNadi.text.toString()
        ancModel.rr = fieldRr.text.toString()
        ancModel.weight = fieldbb.text.toString()
        ancModel.imt = fieldImt.text.toString()
        ancModel.map = fieldMap.text.toString()
        ancModel.rot = fieldRot.text.toString()
        ancModel.age_of_pregnancy = fieldUmurKehamilan.text.toString()
        ancModel.tfu = fieldTfu.text.toString()
        ancModel.fetus_position = fieldLetakJanin.text.toString()
        ancModel.others = fieldLainnya.text.toString()
        ancModel.djj1 = fieldDjj.text.toString()
        ancModel.djj2 = fieldDjj2.text.toString()
        ancModel.swollen_foot = fieldKakiBengkak.text.toString()
        ancModel.blood_type = fieldGoldar.text.toString()
        ancModel.rhesus = fieldRhesus.text.toString()
        ancModel.blood_sugar = fieldGulaDarah.text.toString()
        ancModel.hb = fieldHb.text.toString()
        ancModel.urine_protein = fieldProteinUrin.text.toString()
        ancModel.urine_reduction = fieldReduksiUrin.text.toString()
        ancModel.hepatitis_b = fieldHepatitis.text.toString()
        ancModel.bta = fieldBta.text.toString()
        ancModel.hiv = fieldHiv.text.toString()
        ancModel.malaria = fieldMalaria.text.toString()
        ancModel.sifilis = fieldSifilis.text.toString()
        ancModel.diagnostic = fieldDiagnosa.text.toString()
        ancModel.therapy = fieldTerapi.text.toString()
        ancModel.number_of_immunitation = fieldPemberianImunisasi.text.toString()
        ancModel.return_date = dateComeback
        ancModel.message = fieldNasihat.text.toString()
        ancModel.suggested_weight = fieldSuggestedWeight.text.toString()
        ancModel.differenced_weight = fieldDifferenceBB.text.toString()
        ancModel.initial_weight = fieldBbawal.text.toString()
    }

    private fun isValid(): Boolean {
        if (fieldDifferenceBB.text.toString().equals("")){
            fieldDifferenceBB.setError("Wajib diisi")
            return false
        } else if (fieldKeluhanUtama.text.toString().equals("")){
            fieldKeluhanUtama.setError("Wajib diisi")
            return false
        } else if (fieldSuggestedWeight.text.toString().equals("")){
            fieldSuggestedWeight.setError("Wajib diisi")
            return false
        } else if (fieldKeluhanLainnya.text.toString().equals("")){
            fieldKeluhanLainnya.setError("Wajib diisi")
            return false
        } else if (fieldSistolik.text.toString().equals("")){
            fieldSistolik.setError("Wajib diisi")
            return false
        } else if (fieldbb.text.toString().equals("")){
            fieldbb.setError("Wajib diisi")
            return false
        } else if (fieldDiastolik.text.toString().equals("")){
            fieldDiastolik.setError("Wajib diisi")
            return false
        } else if (fieldImt.text.toString().equals("")){
            fieldImt.setError("Wajib diisi")
            return false
        } else if (fieldNadi.text.toString().equals("")){
            fieldNadi.setError("Wajib diisi")
            return false
        } else if (fieldMap.text.toString().equals("")){
            fieldMap.setError("Wajib diisi")
            return false
        } else if (fieldRr.text.toString().equals("")){
            fieldRr.setError("Wajib diisi")
            return false
        } else if (fieldRot.text.toString().equals("")){
            fieldRot.setError("Wajib diisi")
            return false
        } else if (fieldUmurKehamilan.text.toString().equals("")){
            fieldUmurKehamilan.setError("Wajib diisi")
            return false
        } else if (fieldTfu.text.toString().equals("")){
            fieldTfu.setError("Wajib diisi")
            return false
        } else if (fieldLetakJanin.text.toString().equals("")){
            fieldLetakJanin.setError("Wajib diisi")
            return false
        } else if (fieldLainnya.text.toString().equals("")){
            fieldLainnya.setError("Wajib diisi")
            return false
        } else if (fieldDjj.text.toString().equals("")){
            fieldDjj.setError("Wajib diisi")
            return false
        } else if (fieldKakiBengkak.text.toString().equals("")){
            fieldKakiBengkak.setError("Wajib diisi")
            return false
        } else if (fieldGoldar.text.toString().equals("")){
            fieldGoldar.setError("Wajib diisi")
            return false
        } else if (fieldRhesus.text.toString().equals("")){
            fieldRhesus.setError("Wajib diisi")
            return false
        } else if (fieldHb.text.toString().equals("")){
            fieldHb.setError("Wajib diisi")
            return false
        } else if (fieldProteinUrin.text.toString().equals("")){
            fieldProteinUrin.setError("Wajib diisi")
            return false
        } else if (fieldReduksiUrin.text.toString().equals("")){
            fieldReduksiUrin.setError("Wajib diisi")
            return false
        } else if (fieldHepatitis.text.toString().equals("")){
            fieldHepatitis.setError("Wajib diisi")
            return false
        } else if (fieldBta.text.toString().equals("")){
            fieldBta.setError("Wajib diisi")
            return false
        } else if (fieldHiv.text.toString().equals("")){
            fieldHiv.setError("Wajib diisi")
            return false
        } else if (fieldMalaria.text.toString().equals("")){
            fieldMalaria.setError("Wajib diisi")
            return false
        } else if (fieldSifilis.text.toString().equals("")){
            fieldSifilis.setError("Wajib diisi")
            return false
        } else if (fieldDiagnosa.text.toString().equals("")){
            fieldDiagnosa.setError("Wajib diisi")
            return false
        } else if (fieldTerapi.text.toString().equals("")){
            fieldTerapi.setError("Wajib diisi")
            return false
        } else if (fieldPemberianImunisasi.text.toString().equals("")){
            fieldPemberianImunisasi.setError("Wajib diisi")
            return false
        } else if (fieldTanggal.text.toString().equals("")){
            fieldTanggal.setError("Wajib diisi")
            return false
        } else if (fieldNasihat.text.toString().equals("")){
            fieldNasihat.setError("Wajib diisi")
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
                    dateComeback = "${tanggal}T${dateNow.split("T")[1]}+07:00"
                    fieldTanggal.setText(tanggal)
                    println("date comeback : ${dateComeback}")

                }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.minDate = Date().time
        datePickerDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1){
            if (resultCode== Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), Location::class.java)
                fieldGoldar.setText(dataMaster.name)

            }
        } else if (requestCode==2){
            if (resultCode== Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), Location::class.java)
                fieldRhesus.setText(dataMaster.name)
            }
        } else if (requestCode==3){
            if (resultCode== Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldHepatitis.setText(dataMaster.name)
            }
        } else if (requestCode==4){
            if (resultCode== Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldBta.setText(dataMaster.name)
            }
        } else if (requestCode==5){
            if (resultCode== Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldHiv.setText(dataMaster.name)
            }
        } else if (requestCode==6){
            if (resultCode== Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldMalaria.setText(dataMaster.name)
            }
        } else if (requestCode==7){
            if (resultCode== Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldSifilis.setText(dataMaster.name)
            }
        } else if (requestCode==8){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),Hospital::class.java)
                fieldRujukan.setText(dataMaster.name)
                ancModel.next_hospital = dataMaster
                tv_lokasi_rujukan.text = "Rujukan\nKe\n${dataMaster.level}"
            }
        } else if (requestCode==9){
            var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
            fieldKakiBengkak.setText(dataMaster.name)
        } else if (requestCode==10){
            var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
            fieldProteinUrin.setText(dataMaster.name)
        } else if (requestCode==11){
            var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
            fieldReduksiUrin.setText(dataMaster.name)
        } else if (requestCode==12){
            var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
            fieldLetakJanin.setText(dataMaster.name)
        }
    }
}