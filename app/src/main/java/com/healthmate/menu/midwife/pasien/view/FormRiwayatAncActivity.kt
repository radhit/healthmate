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
import com.healthmate.common.functions.replaceEmpty
import com.healthmate.di.injector
import com.healthmate.menu.midwife.pasien.data.PasienViewModel
import com.healthmate.menu.reusable.data.AncHistory
import com.healthmate.menu.reusable.data.MasterListModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_data_riwayat_anc.*
import kotlinx.android.synthetic.main.activity_data_riwayat_anc.btn_simpan
import kotlinx.android.synthetic.main.activity_data_riwayat_anc.fieldPenolong
import kotlinx.android.synthetic.main.activity_form_ringkasan_persalinan.*
import kotlinx.android.synthetic.main.activity_main_kia.*
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
                setDataInput()
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
        fieldTanggalImunisasi.setOnClickListener {
            callCalender("imunisasi")
        }
        fieldKontrasepsi.setOnClickListener {
            navigator.dataMaster(this,"kontrasepsi",1)
        }
        fieldImunisasi.setOnClickListener {
            navigator.dataMaster(this,"imunisasi",2)
        }
        fieldPenolong.setOnClickListener {
            navigator.dataMaster(this,"penolong persalinan",3)
        }
        fieldCaraPersalinan.setOnClickListener {
            navigator.dataMaster(this,"cara persalinan",4)
        }
    }

    private fun setDataInput() {
        historyAncsModel.hpht = "${fieldHPHT.text.toString()}T07:00:00+07:00"
        historyAncsModel.hpl = "${fieldHPL.text.toString()}T07:00:00+07:00"
        historyAncsModel.lila = fieldLila.text.toString()
        historyAncsModel.height = fieldTinggi.text.toString()
        historyAncsModel.kontrasepsi = fieldKontrasepsi.text.toString()
        historyAncsModel.riwayat_penyakit = fieldRiwayatPenyakit.text.toString()
        historyAncsModel.alergi_obat = fieldRiwayatAlergiObat.text.toString()
        historyAncsModel.alergi_lain = fieldAlergiLain.text.toString()

//        historyAncsModel.preg_num = fieldkehamilan.text.toString()
//        historyAncsModel.labor_num = fieldJumlahPersalinan.text.toString()
//        historyAncsModel.miscarriage_num = fieldJumlahKeguguran.text.toString().replaceEmpty("0")
        historyAncsModel.live_child_num = fieldJumlahAnakHidup.text.toString().replaceEmpty("0")

        historyAncsModel.live_dead_num = fieldJumlahLahirMeninggal.text.toString().replaceEmpty("0")
        historyAncsModel.less_month_child_num = fieldJumlahLahirKurangBulan.text.toString().replaceEmpty("0")

        historyAncsModel.prev_child_difference = fieldJarak.text.toString()

        historyAncsModel.status_imunisasi = fieldImunisasi.text.toString()
        historyAncsModel.date_imunisasi = "${fieldTanggalImunisasi.text.toString()}T07:00:00+07:00"
        historyAncsModel.helper = fieldPenolong.text.toString()
        historyAncsModel.born_method = fieldCaraPersalinan.text.toString()
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
                    } else if (keterangan.equals("hpl")){
                        fieldHPL.setText(tanggal)
                    } else{
                        fieldTanggalImunisasi.setText(tanggal)
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
                        finish()
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
        showLoadingDialog()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(historyAncsModel))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.inputForm("${Urls.registerMother}/${user.id}/anc-history",requestBody)
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
                    Toast.makeText(this@FormRiwayatAncActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormRiwayatAncActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setData() {
        fieldHPHT.setText("${user.anc_history.hpht!!.split("T")[0]}")
        fieldHPL.setText("${user.anc_history.hpl!!.split("T")[0]}")
        fieldLila.setText("${user.anc_history.lila}")
        fieldTinggi.setText("${user.anc_history.height}")
        fieldKontrasepsi.setText("${user.anc_history.kontrasepsi}")
        fieldRiwayatPenyakit.setText("${user.anc_history.riwayat_penyakit}")
        fieldRiwayatAlergiObat.setText("${user.anc_history.alergi_obat}")
        fieldAlergiLain.setText("${user.anc_history.alergi_lain}")
//        fieldkehamilan.setText("${user.anc_history.preg_num}")
//        fieldJumlahPersalinan.setText("${user.anc_history.labor_num}")
//        fieldJumlahKeguguran.setText("${user.anc_history.miscarriage_num}")
        fieldJumlahAnakHidup.setText("${user.anc_history.live_child_num}")
        fieldJarak.setText("${user.anc_history.prev_child_difference}")

        fieldJumlahLahirMeninggal.setText("${user.anc_history.live_dead_num}")
        fieldJumlahLahirKurangBulan.setText("${user.anc_history.less_month_child_num}")
        fieldImunisasi.setText("${user.anc_history.status_imunisasi}")
        fieldTanggalImunisasi.setText("${user.anc_history.date_imunisasi!!.split("T")[0]}")
        fieldPenolong.setText("${user.anc_history.helper}")
        fieldCaraPersalinan.setText("${user.anc_history.born_method}")
    }

    private fun isValid(): Boolean {
        if (fieldHPHT.text.toString().equals("")){
            fieldHPHT.setError("Wajib diisi")
            return false
        } else if (fieldHPL.text.toString().equals("")){
            fieldHPL.setError("Wajib diisi")
            return false
        }
//        else if (fieldkehamilan.text.toString().equals("")){
//            fieldkehamilan.setError("Wajib diisi")
//            return false
//        } else if (fieldJumlahPersalinan.text.toString().equals("")){
//            fieldJumlahPersalinan.setError("Wajib diisi")
//            return false
//        } else if (fieldJumlahKeguguran.text.toString().equals("")){
//            fieldJumlahKeguguran.setError("Wajib diisi")
//            return false
//        }
        else if (fieldJumlahAnakHidup.text.toString().equals("")){
            fieldJumlahAnakHidup.setError("Wajib diisi")
            return false
        } else if (fieldJarak.text.toString().equals("")){
            fieldJarak.setError("Wajib diisi")
            return false
        } else if (fieldLila.text.toString().equals("")){
            fieldLila.setError("Wajib diisi")
            return false
        } else if (fieldTinggi.text.toString().equals("")){
            fieldTinggi.setError("Wajib diisi")
            return false
        } else if (fieldKontrasepsi.text.toString().equals("")){
            fieldKontrasepsi.setError("Wajib diisi")
            return false
        } else if (fieldJumlahLahirMeninggal.text.toString().equals("")){
            fieldJumlahLahirMeninggal.setError("Wajib diisi")
            return false
        } else if (fieldJumlahLahirKurangBulan.text.toString().equals("")){
            fieldJumlahLahirKurangBulan.setError("Wajib diisi")
            return false
        } else if (fieldImunisasi.text.toString().equals("")){
            fieldImunisasi.setError("Wajib diisi")
            return false
        } else if (fieldTanggalImunisasi.text.toString().equals("")){
            fieldTanggalImunisasi.setError("Wajib diisi")
            return false
        } else if (fieldPenolong.text.toString().equals("")){
            fieldPenolong.setError("Wajib diisi")
            return false
        } else if (fieldCaraPersalinan.text.toString().equals("")){
            fieldCara.setError("Wajib diisi")
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1){
            var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
            fieldKontrasepsi.setText(dataMaster.name)
        } else if (requestCode==2){
            var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
            fieldImunisasi.setText(dataMaster.name)
        } else if (requestCode==3){
            var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
            fieldPenolong.setText(dataMaster.name)
        } else if (requestCode==4){
            var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
            fieldCaraPersalinan.setText(dataMaster.name)
        }
    }
}