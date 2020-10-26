package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
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
        val EXTRA_KETERANGAN = "EXTRA_KETERANGAN"
        val EXTRA_ANC = "EXTRA_ANC"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String, keterangan: String, dataAnc: String): Intent {
            val intent = Intent(activity, FormInputAncActivity::class.java)
            intent.putExtra(EXTRA, data)
            intent.putExtra(EXTRA_KETERANGAN, keterangan)
            intent.putExtra(EXTRA_ANC, dataAnc)
            return intent
        }
    }

    override fun getView(): Int = R.layout.activity_form_input_anc
    var ancModel: AncModel = AncModel()
    var mother: User = User()
    var hospital: Hospital = Hospital()
    var dateNow: String = ""
    var dateComeback: String = ""
    var dataKeluhan: ArrayList<String> = arrayListOf()
    var isError: Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Formulir ANC")
        mother = gson.fromJson(intent.getStringExtra(EXTRA), User::class.java)
        ll_form.visibility = View.VISIBLE
        ll_hospital.visibility = View.GONE
        if (intent.getStringExtra(EXTRA_KETERANGAN).equals("detil")){
            setViewDetil()
        }


        var current = LocalDateTime.now().toString()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        dateNow = current.format(formatter)
        println("date comeback : ${dateNow}")
        tv_tanggal.text = dateNow.split("T")[0]
        fieldGoldar.setOnClickListener {
            navigator.dataMaster(this, "goldar", 1)
        }
        fieldRhesus.setOnClickListener {
            navigator.dataMaster(this, "rhesus", 2)
        }
        fieldHepatitis.setOnClickListener {
            navigator.dataMaster(this, "hepatitis", 3)
        }
        fieldBta.setOnClickListener {
            navigator.dataMaster(this, "bta", 4)
        }
        fieldHiv.setOnClickListener {
            navigator.dataMaster(this, "hiv", 5)
        }
        fieldMalaria.setOnClickListener {
            navigator.dataMaster(this, "malaria", 6)
        }
        fieldSifilis.setOnClickListener {
            navigator.dataMaster(this, "sifilis", 7)
        }
        fieldTanggal.setOnClickListener {
            callCalender()
        }
        fieldKeluhanUtama.setOnClickListener {
            navigator.dataSelectable(this, "Keluhan", 13)
        }

        fieldRujukan.setOnClickListener {
            navigator.listLocation(this, "rujukan", 8, gson.toJson(mother))
        }
        fieldKakiBengkak.setOnClickListener {
            navigator.dataMaster(this, "kaki bengkak", 9)
        }
        fieldProteinUrin.setOnClickListener {
            navigator.dataMaster(this, "protein urin", 10)
        }
        fieldReduksiUrin.setOnClickListener {
            navigator.dataMaster(this, "reduksi urin", 11)
        }
        fieldLetakJanin.setOnClickListener {
            navigator.dataMaster(this, "letak janin", 12)
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
        btn_verif.setOnClickListener {
//            if (isError){
//            }
            removeError()
            if (isValid()){
                setData()
                submit()
            }
        }
        btn_simpan.setOnClickListener {
            if (fieldRujukan.text.toString().equals("")){
                fieldRujukan.setError("Wajib diisi")
            } else{
                postAnc()
            }
        }

    }

    private fun removeError() {
        inputKeluhanUtama.setError(null)
        inputSuggestedWeight.setError(null)
        inputKeluhanLain.setError(null)
        inputSistolik.setError(null)
        inputbb.setError(null)
        inputDiastolik.setError(null)
        inputNadi.setError(null)
        inputMap.setError(null)
        inputRR.setError(null)
        inputRot.setError(null)
        inputUmurKehamilan.setError(null)
        inputTfu.setError(null)
        inputLetakJanin.setError(null)
        inputDjj1.setError(null)
        inputKakiBengkak.setError(null)
        inputGoldar.setError(null)
        inputRhesus.setError(null)
        inputHb.setError(null)
        inputProteinUrin.setError(null)
        inputReduksiUrin.setError(null)
        inputHepatitis.setError(null)
        inputBta.setError(null)
        inputHiv.setError(null)
        inputMalaria.setError(null)
        inputSifilis.setError(null)
        inputDiagnosa.setError(null)
        inputTerapi.setError(null)
        inputPemberian.setError(null)
        inputTanggal.setError(null)
        inputNasihat.setError(null)
        inputDjj2.setError(null)
    }

    private fun setViewDetil() {
        ancModel = gson.fromJson(intent.getStringExtra(EXTRA_ANC),AncModel::class.java)
        ll_hospital.visibility = View.VISIBLE
        inputImt.visibility = View.VISIBLE
        inputPerbedaan.visibility = View.VISIBLE
        var complaint = ""
        for (data in ancModel.complaint){
            complaint="${complaint}${data}, "
        }
        fieldKeluhanUtama.setText(complaint.substring(0,complaint.length-2))
        fieldKeluhanUtama.isEnabled = false
        fieldKeluhanLainnya.setText(ancModel.other_complaint)
        fieldKeluhanLainnya.isEnabled = false
        fieldSistolik.setText(ancModel.sistolik.toString())
        fieldSistolik.isEnabled = false
        fieldbb.setText(ancModel.weight.toString())
        fieldbb.isEnabled = false
        fieldBbawal.setText(ancModel.initial_weight.toString())
        fieldBbawal.isEnabled = false
        fieldSuggestedWeight.setText(ancModel.suggested_weight)
        fieldSuggestedWeight.isEnabled = false
        fieldDiastolik.setText(ancModel.diastolik.toString())
        fieldDiastolik.isEnabled = false
        fieldDifferenceBB.setText(ancModel.differenced_weight.toString())
        fieldDifferenceBB.isEnabled = false
        fieldNadi.setText(ancModel.nadi.toString())
        fieldNadi.isEnabled = false
        fieldImt.setText(ancModel.imt.toString())
        fieldImt.isEnabled = false
        fieldRr.setText(ancModel.rr.toString())
        fieldRr.isEnabled = false
        fieldMap.setText(ancModel.map.toString())
        fieldMap.isEnabled = false
        fieldRot.setText(ancModel.rot)
        fieldRot.isEnabled = false
        fieldUmurKehamilan.setText(ancModel.age_of_pregnancy.toString())
        fieldUmurKehamilan.isEnabled = false
        fieldTfu.setText(ancModel.tfu.toString())
        fieldTfu.isEnabled = false
        fieldLetakJanin.setText(ancModel.fetus_position)
        fieldLetakJanin.isEnabled = false
//        fieldLainnya.setText(ancModel.others)
//        fieldLainnya.isEnabled = false
        fieldDjj.setText(ancModel.djj[0].toString())
        fieldDjj.isEnabled = false
        rb_tunggal.isEnabled = false
        rb_kembar.isEnabled = false
        if (ancModel.djj.size>1){
            rb_tunggal.isChecked = false
            rb_kembar.isChecked = true
            fieldDjj2.visibility = View.VISIBLE
            fieldDjj2.setText(ancModel.djj[1].toString())
            fieldDjj2.isEnabled = false
        } else{
            rb_tunggal.isChecked = true
            rb_kembar.isChecked = false
        }
        fieldKakiBengkak.setText(ancModel.swollen_foot)
        fieldKakiBengkak.isEnabled = false
        fieldGoldar.setText(ancModel.blood_type)
        fieldGoldar.isEnabled = false
        fieldRhesus.setText(ancModel.rhesus)
        fieldRhesus.isEnabled = false
        fieldGulaDarah.setText(ancModel.blood_sugar)
        fieldGulaDarah.isEnabled = false
        fieldHb.setText(ancModel.hb.toString())
        fieldHb.isEnabled = false
        fieldProteinUrin.setText(ancModel.urine_protein)
        fieldProteinUrin.isEnabled = false
        fieldReduksiUrin.setText(ancModel.urine_reduction)
        fieldReduksiUrin.isEnabled = false
        fieldHepatitis.setText(ancModel.hepatitis_b)
        fieldHepatitis.isEnabled = false
        fieldBta.setText(ancModel.bta)
        fieldBta.isEnabled = false
        fieldHiv.setText(ancModel.hiv)
        fieldHiv.isEnabled = false
        fieldMalaria.setText(ancModel.malaria)
        fieldMalaria.isEnabled = false
        fieldSifilis.setText(ancModel.sifilis)
        fieldSifilis.isEnabled = false
        fieldDiagnosa.setText(ancModel.diagnostic)
        fieldDiagnosa.isEnabled = false
        fieldTerapi.setText(ancModel.therapy)
        fieldTerapi.isEnabled = false
        fieldPemberianImunisasi.setText(ancModel.number_of_immunitation)
        fieldPemberianImunisasi.isEnabled = false
        fieldTanggal.setText(ancModel.return_date.split("T")[0])
        fieldTanggal.isEnabled = false
        fieldNasihat.setText(ancModel.message)
        fieldNasihat.isEnabled = false
        btn_verif.visibility = View.GONE
        tv_lokasi_rujukan.setText("Rujukan Ke\n${ancModel.next_hospital!!.name}")
        inputLokasi.visibility = View.GONE
        btn_simpan.visibility = View.GONE
        if (ancModel.diagnostic_color.equals("red")) {
            Glide.with(this@FormInputAncActivity).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.status_red)).into(iv_status)
        } else if (ancModel.diagnostic_color.equals("green")) {
            Glide.with(this@FormInputAncActivity).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.status_green)).into(iv_status)
        } else {
            Glide.with(this@FormInputAncActivity).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.status_yellow)).into(iv_status)
        }
    }

    private fun postAnc() {
        showLoadingDialog()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(ancModel))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.inputForm("${Urls.ancsMom}", requestBody)
        call.enqueue(object : Callback<DataResponse<Any>> {
            override fun onResponse(call: Call<DataResponse<Any>>?, response: Response<DataResponse<Any>>?) {
                closeLoadingDialog()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299) {
                        dialogFinish()
                    } else {
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@FormInputAncActivity, "Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormInputAncActivity, t!!.message, Toast.LENGTH_LONG).show()
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
        val call: Call<DataResponse<AncModel>> = baseApi.validatorAnc("/ancs-validator", requestBody)
        call.enqueue(object : Callback<DataResponse<AncModel>> {
            override fun onResponse(call: Call<DataResponse<AncModel>>?, response: Response<DataResponse<AncModel>>?) {
                closeLoadingDialog()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299) {
                        println("data validator ${gson.toJson(response.body()!!.data)}")
                        ancModel.diagnostic_color = response.body()!!.data!!.diagnostic_color
                        println("data anc ${gson.toJson(ancModel)}")
                        ll_form.visibility = View.GONE
                        ll_hospital.visibility = View.VISIBLE
                        if (ancModel.diagnostic_color.equals("red")) {
                            Glide.with(this@FormInputAncActivity).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.status_red)).into(iv_status)
                        } else if (ancModel.diagnostic_color.equals("green")) {
                            Glide.with(this@FormInputAncActivity).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.status_green)).into(iv_status)
                        } else {
                            Glide.with(this@FormInputAncActivity).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.status_yellow)).into(iv_status)
                        }
                    } else {
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@FormInputAncActivity, "Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<AncModel>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormInputAncActivity, t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setData() {
        ancModel.mother_id = mother.id
        ancModel.midwife_id = userPref.getUser().id
        ancModel.date = "${dateNow}+07:00"
        ancModel.complaint.addAll(dataKeluhan)
        ancModel.other_complaint = fieldKeluhanLainnya.text.toString()
        ancModel.sistolik = fieldSistolik.text.toString().toInt()
        ancModel.initial_weight = fieldBbawal.text.toString().toInt()
        ancModel.diastolik = fieldDiastolik.text.toString().toInt()
        ancModel.nadi = fieldNadi.text.toString().toInt()
        ancModel.rr = fieldRr.text.toString().toInt()
        ancModel.weight = fieldbb.text.toString().toInt()
        ancModel.suggested_weight = fieldSuggestedWeight.text.toString()
//        ancModel.differenced_weight = fieldDifferenceBB.text.toString().toInt()
//        ancModel.imt = fieldImt.text.toString().toInt()
        ancModel.map = fieldMap.text.toString().toInt()
        ancModel.rot = fieldRot.text.toString()
        ancModel.age_of_pregnancy = fieldUmurKehamilan.text.toString().toInt()
        ancModel.tfu = fieldTfu.text.toString().toInt()
        ancModel.fetus_position = fieldLetakJanin.text.toString()
//        ancModel.others = fieldLainnya.text.toString()
        ancModel.djj.clear()
        if (rb_kembar.isChecked){
            ancModel.djj.add(fieldDjj.text.toString().toInt())
            ancModel.djj.add(fieldDjj2.text.toString().toInt())
        } else{
            ancModel.djj.add(fieldDjj.text.toString().toInt())
        }
        ancModel.swollen_foot = fieldKakiBengkak.text.toString()
        ancModel.blood_type = fieldGoldar.text.toString()
        ancModel.rhesus = fieldRhesus.text.toString()
        ancModel.blood_sugar = fieldGulaDarah.text.toString()
        ancModel.hb = fieldHb.text.toString().toInt()
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

    }

    private fun isValid(): Boolean {
//        if (fieldDifferenceBB.text.toString().equals("")){
//            fieldDifferenceBB.setError("Wajib diisi")
//            return false
//        } else
        if (fieldKeluhanUtama.text.toString().equals("")){
            inputKeluhanUtama.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldSuggestedWeight.text.toString().equals("")){
            inputSuggestedWeight.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldKeluhanLainnya.text.toString().equals("")){
            inputKeluhanLain.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldSistolik.text.toString().equals("")){
            inputSistolik.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldbb.text.toString().equals("")){
            inputbb.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldDiastolik.text.toString().equals("")){
            inputDiastolik.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        }
//        else if (fieldImt.text.toString().equals("")){
//            fieldImt.setError("Wajib diisi")
//            return false
//        }
        else if (fieldNadi.text.toString().equals("")){
            inputNadi.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldMap.text.toString().equals("")){
            inputMap.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldRr.text.toString().equals("")){
            inputRR.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldRot.text.toString().equals("")){
            inputRot.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldUmurKehamilan.text.toString().equals("")){
            inputUmurKehamilan.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldTfu.text.toString().equals("")){
            inputTfu.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldLetakJanin.text.toString().equals("")){
            inputLetakJanin.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        }
//        else if (fieldLainnya.text.toString().equals("")){
//            fieldLainnya.setError("Wajib diisi")
//            return false
//        }
        else if (fieldDjj.text.toString().equals("")){
            inputDjj1.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldKakiBengkak.text.toString().equals("")){
            inputKakiBengkak.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldGoldar.text.toString().equals("")){
            inputGoldar.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldRhesus.text.toString().equals("")){
            inputRhesus.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldHb.text.toString().equals("")){
            inputHb.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldProteinUrin.text.toString().equals("")){
            inputProteinUrin.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldReduksiUrin.text.toString().equals("")){
            inputReduksiUrin.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldHepatitis.text.toString().equals("")){
            inputHepatitis.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldBta.text.toString().equals("")){
            inputBta.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldHiv.text.toString().equals("")){
            inputHiv.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldMalaria.text.toString().equals("")){
            inputMalaria.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldSifilis.text.toString().equals("")){
            inputSifilis.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldDiagnosa.text.toString().equals("")){
            inputDiagnosa.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldTerapi.text.toString().equals("")){
            inputTerapi.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldPemberianImunisasi.text.toString().equals("")){
            inputPemberian.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldTanggal.text.toString().equals("")){
            inputTanggal.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        } else if (fieldNasihat.text.toString().equals("")){
            inputNasihat.setError("Wajib diisi")
            Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
            return false
        }
        if (rb_kembar.isChecked){
            if (fieldDjj2.text.toString().equals("")){
                inputDjj2.setError("Wajib diisi")
                Toast.makeText(this@FormInputAncActivity, "Data formulir tidak lengkap!", Toast.LENGTH_LONG).show()
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
                    dateComeback = "${tanggal}T${dateNow.split("T")[1]}+07:00"
                    fieldTanggal.setText(tanggal)
                }, mYear, mMonth, mDay)

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
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), Hospital::class.java)
                fieldRujukan.setText(dataMaster.name)
                ancModel.next_hospital = dataMaster
                tv_lokasi_rujukan.text = "Rujukan\nKe\n${dataMaster.level}"
            }
        } else if (requestCode==9){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldKakiBengkak.setText(dataMaster.name)
            }
        } else if (requestCode==10){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldProteinUrin.setText(dataMaster.name)
            }
        } else if (requestCode==11){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldReduksiUrin.setText(dataMaster.name)
            }
        } else if (requestCode==12){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldLetakJanin.setText(dataMaster.name)
            }
        } else if (requestCode==13){
            if (resultCode== RESULT_OK){
                dataKeluhan.clear()
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), JsonArray::class.java)
                println("data master : ${gson.toJson(dataMaster)}")
                var daftarKeluhan = ""

                for (i in 0..dataMaster.size()-1){
                    dataKeluhan.add(dataMaster[i].toString().substring(1, dataMaster[i].toString().length - 1))
                    daftarKeluhan="${daftarKeluhan}${dataMaster[i].toString().substring(1, dataMaster[i].toString().length - 1)}, "
                }
                fieldKeluhanUtama.setText(daftarKeluhan.substring(0, daftarKeluhan.length - 2))
            }
        }
    }
}