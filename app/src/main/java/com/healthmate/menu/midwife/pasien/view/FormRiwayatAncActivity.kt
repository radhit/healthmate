package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.healthmate.R
import com.healthmate.api.DataResponse
import com.healthmate.api.Payload
import com.healthmate.api.PayloadEntry
import com.healthmate.api.Result
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.di.injector
import com.healthmate.menu.midwife.pasien.data.PasienViewModel
import com.healthmate.menu.reusable.data.AncHistory
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_data_riwayat_anc.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FormRiwayatAncActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        val EXTRA_KETERANGAN = "EXTRA_KETERANGAN"
        @JvmStatic
        fun getCallingIntent(activity: Activity, keterangan: String, data: String): Intent {
            val intent = Intent(activity, FormRiwayatAncActivity::class.java)
            intent.putExtra(EXTRA, data)
            intent.putExtra(EXTRA_KETERANGAN, keterangan)
            return intent
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.pasienVM()).get(PasienViewModel::class.java)
    }

    override fun getView(): Int = R.layout.activity_data_riwayat_anc
    var keterangan: String = ""
    var user: User = User()
    var historyAncsModel: AncHistory = AncHistory()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        keterangan = intent.getStringExtra(EXTRA_KETERANGAN)
        user = gson.fromJson(intent.getStringExtra(EXTRA),User::class.java)
        this.setTitle("Data History Anc")
        if (keterangan.equals("edit")){
            setData()
        }
        btn_simpan.setOnClickListener {
            if (isValid()){
                historyAncsModel.hpht = fieldHPHT.text.toString()
                historyAncsModel.hml = fieldHPL.text.toString()
                historyAncsModel.preg_num = fieldkehamilan.text.toString()
                historyAncsModel.labor_num = fieldJumlahPersalinan.text.toString()
                historyAncsModel.miscarriage_num = fieldJumlahKeguguran.text.toString()
                historyAncsModel.live_child_num = fieldJumlahAnakHidup.text.toString()
                historyAncsModel.prev_child_difference = fieldJarak.text.toString()
                if (keterangan.equals("edit")){
                    historyAncsModel.id = user.anc_history.id
                    update()
                } else{
                    submit()
                }
            }
        }
        fieldHPHT.setOnClickListener {
            callCalender("hpht")
        }
        fieldHPL.setOnClickListener {
            callCalender("hpl")
        }
    }

    private fun callCalender(keterangan: String) {
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
                    if (keterangan.equals("hpht")){
                        fieldHPHT.setText(tanggal)
                    } else{
                        fieldHPL.setText(tanggal)
                    }
                }, mYear, mMonth, mDay)

        datePickerDialog.show()
    }

    private fun update() {
        showLoadingDialog()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(historyAncsModel))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.updateDataAncsHistory("${Urls.ancsHistory}/${user.anc_history.id}",requestBody)
        call.enqueue(object : Callback<DataResponse<Any>> {
            override fun onResponse(call: Call<DataResponse<Any>>?, response: Response<DataResponse<Any>>?) {
                closeLoadingDialog()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299){
                        createDialog(response.body()!!.message,{
                            finish()
                        })
                    } else{
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@FormRiwayatAncActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormRiwayatAncActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun submit() {
        val payload = Payload()
        payload.url = "${Urls.registerMother}/${user.id}/anc-history"
        payload.payloads.add(PayloadEntry("hpht",historyAncsModel.hpht!!))
        payload.payloads.add(PayloadEntry("hpl",historyAncsModel.hml!!))
        payload.payloads.add(PayloadEntry("preg_num",historyAncsModel.preg_num!!))
        payload.payloads.add(PayloadEntry("labor_num",historyAncsModel.labor_num!!))
        payload.payloads.add(PayloadEntry("miscarriage_num",historyAncsModel.miscarriage_num!!))
        payload.payloads.add(PayloadEntry("live_child_num",historyAncsModel.live_child_num!!))
        payload.payloads.add(PayloadEntry("prev_child_difference",historyAncsModel.prev_child_difference!!))
        viewModel.postDataAncsHistory(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            showLoadingDialog()
                        }
                        Result.Status.SUCCESS->{
                            closeLoadingDialog()
                            createDialog(result.message!!,{
                                finish()
                            })
                        }
                        Result.Status.ERROR->{
                            closeLoadingDialog()
                            createDialog(result.message!!)
                        }
                    }
                })
    }

    private fun setData() {
        fieldHPHT.setText("${user.anc_history.hpht}")
        fieldHPL.setText("${user.anc_history.hml}")
        fieldkehamilan.setText("${user.anc_history.preg_num}")
        fieldJumlahPersalinan.setText("${user.anc_history.labor_num}")
        fieldJumlahKeguguran.setText("${user.anc_history.miscarriage_num}")
        fieldJumlahAnakHidup.setText("${user.anc_history.live_child_num}")
        fieldJarak.setText("${user.anc_history.prev_child_difference}")
    }

    private fun isValid(): Boolean {
        if (fieldHPHT.text.toString().equals("")){
            fieldHPHT.setError("Wajib diisi")
            return false
        } else if (fieldHPL.text.toString().equals("")){
            fieldHPL.setError("Wajib diisi")
            return false
        } else if (fieldkehamilan.text.toString().equals("")){
            fieldkehamilan.setError("Wajib diisi")
            return false
        } else if (fieldJumlahPersalinan.text.toString().equals("")){
            fieldJumlahPersalinan.setError("Wajib diisi")
            return false
        } else if (fieldJumlahKeguguran.text.toString().equals("")){
            fieldJumlahKeguguran.setError("Wajib diisi")
            return false
        } else if (fieldJumlahAnakHidup.text.toString().equals("")){
            fieldJumlahAnakHidup.setError("Wajib diisi")
            return false
        } else if (fieldJarak.text.toString().equals("")){
            fieldJarak.setError("Wajib diisi")
            return false
        }
        return true
    }
}