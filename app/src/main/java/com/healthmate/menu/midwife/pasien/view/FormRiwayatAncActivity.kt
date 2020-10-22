package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.healthmate.R
import com.healthmate.api.DataResponse
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.replaceEmpty
import com.healthmate.di.injector
import com.healthmate.menu.midwife.pasien.data.PasienViewModel
import com.healthmate.menu.reusable.data.AncHistory
import com.healthmate.menu.reusable.data.MasterListModel
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
    var dataPenyakit: ArrayList<String> = arrayListOf()
    val MODE_SPINNER = 1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        keterangan = intent.getStringExtra(EXTRA_KETERANGAN)
        user = gson.fromJson(intent.getStringExtra(EXTRA), User::class.java)
        this.setTitle("Data History Anc")
        if (keterangan.equals("edit")){
            setData()
        }
        btn_simpan.setOnClickListener {
            removeError()
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
            navigator.dataMaster(this, "kontrasepsi", 1)
        }
        fieldImunisasi.setOnClickListener {
            navigator.dataMaster(this, "imunisasi", 2)
        }
        fieldPenolong.setOnClickListener {
            navigator.dataMaster(this, "penolong persalinan", 3)
        }
        fieldCaraPersalinan.setOnClickListener {
            navigator.dataMaster(this, "cara persalinan", 4)
        }
        fieldRiwayatPenyakit.setOnClickListener {
            navigator.dataSelectable(this, "Riwayat Penyakit", 5)
        }
    }

    private fun removeError() {
        inputHpht.setError(null)
        inputHpl.setError(null)
        inputJumlahAnakHidup.setError(null)
        inputJarak.setError(null)
        inputJarakBulan.setError(null)
        inputLila.setError(null)
        inputTinggi.setError(null)
        inputKontrasepsi.setError(null)
        inputJumlahLahirMeninggal.setError(null)
        inputJumlahLahirKurangBulan.setError(null)
        inputImunisasi.setError(null)
        inputTangglImunisasi.setError(null)
        inputPenolong.setError(null)
        inputCaraPersalinan.setError(null)
        inputG.setError(null)
        inputP.setError(null)
        inputA.setError(null)
    }

    private fun setDataInput() {
        historyAncsModel.hpht = "${fieldHPHT.text.toString()}T07:00:00+07:00"
        historyAncsModel.hpl = "${fieldHPL.text.toString()}T07:00:00+07:00"
        historyAncsModel.lila = fieldLila.text.toString().replaceEmpty("0").toDouble()
        if (historyAncsModel.lila!!<23.5){
            historyAncsModel.lila_status = "KEK"
        } else{
            historyAncsModel.lila_status = "NON KEK"
        }
        historyAncsModel.height = fieldTinggi.text.toString().replaceEmpty("0").toDouble()
        historyAncsModel.kontrasepsi = fieldKontrasepsi.text.toString()
        historyAncsModel.riwayat_penyakit.addAll(dataPenyakit)
        historyAncsModel.alergi_obat = fieldRiwayatAlergiObat.text.toString()
        historyAncsModel.alergi_lain = fieldAlergiLain.text.toString()

//        historyAncsModel.preg_num = fieldkehamilan.text.toString()
//        historyAncsModel.labor_num = fieldJumlahPersalinan.text.toString()
//        historyAncsModel.miscarriage_num = fieldJumlahKeguguran.text.toString().replaceEmpty("0")

        historyAncsModel.g = fieldG.text.toString().replaceEmpty("0").toInt()
        historyAncsModel.p = fieldP.text.toString().replaceEmpty("0").toInt()
        historyAncsModel.a = fieldA.text.toString().replaceEmpty("0").toInt()
        historyAncsModel.live_child_num = fieldJumlahAnakHidup.text.toString().replaceEmpty("0").toInt()

        historyAncsModel.dead_child_num = fieldJumlahLahirMeninggal.text.toString().replaceEmpty("0").toInt()
        historyAncsModel.less_month_child_num = fieldJumlahLahirKurangBulan.text.toString().replaceEmpty("0").toInt()

        historyAncsModel.prev_child_year = fieldJarak.text.toString().toInt()
        historyAncsModel.prev_child_month = fieldJarakBulan.text.toString().toInt()

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
                    if (keterangan.equals("hpht")) {
                        fieldHPHT.setText(tanggal)
                    } else if (keterangan.equals("hpl")) {
                        fieldHPL.setText(tanggal)
                    } else {
                        fieldTanggalImunisasi.setText(tanggal)
                    }
                }, mYear, mMonth, mDay)

        datePickerDialog.show()
    }

    private fun update() {
        showLoadingDialog()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(historyAncsModel))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.updateDataAncsHistory("${Urls.ancsHistory}/${user.anc_history.id}", requestBody)
        call.enqueue(object : Callback<DataResponse<Any>> {
            override fun onResponse(call: Call<DataResponse<Any>>?, response: Response<DataResponse<Any>>?) {
                closeLoadingDialog()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299) {
                        finish()
                    } else {
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@FormRiwayatAncActivity, "Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormRiwayatAncActivity, t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun submit() {
        showLoadingDialog()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(historyAncsModel))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.inputForm("${Urls.registerMother}/${user.id}/anc-history", requestBody)
        call.enqueue(object : Callback<DataResponse<Any>> {
            override fun onResponse(call: Call<DataResponse<Any>>?, response: Response<DataResponse<Any>>?) {
                closeLoadingDialog()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299) {
                        finish()
                    } else {
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@FormRiwayatAncActivity, "Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                closeLoadingDialog()
                Toast.makeText(this@FormRiwayatAncActivity, t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setData() {
        fieldHPHT.setText("${user.anc_history.hpht!!.split("T")[0]}")
        fieldHPL.setText("${user.anc_history.hpl!!.split("T")[0]}")
        fieldLila.setText("${user.anc_history.lila}")
        fieldTinggi.setText("${user.anc_history.height}")
        fieldKontrasepsi.setText("${user.anc_history.kontrasepsi}")
        var daftarPenyakit = ""
        dataPenyakit.addAll(user.anc_history.riwayat_penyakit)
        for (i in 0..dataPenyakit.size-1){
            daftarPenyakit="${daftarPenyakit}${dataPenyakit[i].toString().substring(1,dataPenyakit[i].toString().length-1)}, "
        }
        println("data Penyakit: ${gson.toJson(dataPenyakit)}")
        if (dataPenyakit.size>0){
            fieldRiwayatPenyakit.setText(daftarPenyakit)
        }

        fieldRiwayatAlergiObat.setText("${user.anc_history.alergi_obat}")
        fieldAlergiLain.setText("${user.anc_history.alergi_lain}")
//        fieldkehamilan.setText("${user.anc_history.preg_num}")
//        fieldJumlahPersalinan.setText("${user.anc_history.labor_num}")
//        fieldJumlahKeguguran.setText("${user.anc_history.miscarriage_num}")
        fieldG.setText("${user.anc_history.g}")
        fieldP.setText("${user.anc_history.p}")
        fieldA.setText("${user.anc_history.a}")
        fieldJumlahAnakHidup.setText("${user.anc_history.live_child_num}")
        fieldJarak.setText("${user.anc_history.prev_child_year}")
        fieldJarakBulan.setText("${user.anc_history.prev_child_month}")

        fieldJumlahLahirMeninggal.setText("${user.anc_history.dead_child_num}")
        fieldJumlahLahirKurangBulan.setText("${user.anc_history.less_month_child_num}")
        fieldImunisasi.setText("${user.anc_history.status_imunisasi}")
        fieldTanggalImunisasi.setText("${user.anc_history.date_imunisasi!!.split("T")[0]}")
        fieldPenolong.setText("${user.anc_history.helper}")
        fieldCaraPersalinan.setText("${user.anc_history.born_method}")
    }

    private fun isValid(): Boolean {
        if (fieldHPHT.text.toString().equals("")){
            inputHpht.setError("Wajib diisi")
            return false
        } else if (fieldHPL.text.toString().equals("")){
            inputHpl.setError("Wajib diisi")
            return false
        }
        else if (fieldJumlahAnakHidup.text.toString().equals("")){
            inputJumlahAnakHidup.setError("Wajib diisi")
            return false
        } else if (fieldJarak.text.toString().equals("")){
            inputJarak.setError("Wajib diisi")
            return false
        } else if (fieldJarakBulan.text.toString().equals("")){
            inputJarakBulan.setError("Wajib diisi")
            return false
        } else if (fieldLila.text.toString().equals("")){
            inputLila.setError("Wajib diisi")
            return false
        } else if (fieldTinggi.text.toString().equals("")){
            inputTinggi.setError("Wajib diisi")
            return false
        } else if (fieldKontrasepsi.text.toString().equals("")){
            inputKontrasepsi.setError("Wajib diisi")
            return false
        } else if (fieldJumlahLahirMeninggal.text.toString().equals("")){
            inputJumlahLahirMeninggal.setError("Wajib diisi")
            return false
        } else if (fieldJumlahLahirKurangBulan.text.toString().equals("")){
            inputJumlahLahirKurangBulan.setError("Wajib diisi")
            return false
        } else if (fieldImunisasi.text.toString().equals("")){
            inputImunisasi.setError("Wajib diisi")
            return false
        } else if (fieldTanggalImunisasi.text.toString().equals("")){
            inputTangglImunisasi.setError("Wajib diisi")
            return false
        } else if (fieldPenolong.text.toString().equals("")){
            inputPenolong.setError("Wajib diisi")
            return false
        } else if (fieldCaraPersalinan.text.toString().equals("")){
            inputCaraPersalinan.setError("Wajib diisi")
            return false
        } else if (fieldG.text.toString().equals("")){
            inputG.setError("Wajib diisi")
            return false
        } else if (fieldP.text.toString().equals("")){
            inputP.setError("Wajib diisi")
            return false
        } else if (fieldA.text.toString().equals("")){
            inputA.setError("Wajib diisi")
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK){
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
            } else if (requestCode==5){
                dataPenyakit.clear()
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), JsonArray::class.java)
                println("data master : ${gson.toJson(dataMaster)}")
                var daftarPenyakit = ""

                for (i in 0..dataMaster.size()-1){
                    dataPenyakit.add(dataMaster[i].toString().substring(1,dataMaster[i].toString().length-1))
                    daftarPenyakit="${daftarPenyakit}${dataMaster[i].toString().substring(1,dataMaster[i].toString().length-1)}, "
                }
                println("data Penyakit: ${gson.toJson(dataPenyakit)}")
                fieldRiwayatPenyakit.setText(daftarPenyakit.substring(0, daftarPenyakit.length - 2))
            }

        }
    }
}