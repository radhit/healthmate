package com.healthmate.menu.mom.kia.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.menu.mom.covid.view.ScreeningCovidActivity
import com.healthmate.menu.reusable.data.Husband
import com.healthmate.menu.reusable.data.Kia
import com.healthmate.menu.reusable.data.Location
import com.healthmate.menu.reusable.data.MasterListModel
import kotlinx.android.synthetic.main.activity_main_kia.*
import java.text.SimpleDateFormat
import java.util.*

class MainKiaActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            val intent = Intent(activity, MainKiaActivity::class.java)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_main_kia
    var dataKia: Kia = Kia()
    var id_kabupaten: String = ""
    var id_kecamatan: String = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Data KIA")
        if (userPref.getUser().kia!=null){
            dataKia = userPref.getUser().kia!!
            setData()
        }
        fieldNama.setText("${userPref.getUser().name}")
        btn_simpan.setOnClickListener {
            if (isValid()){
                var user = userPref.getUser()
                setDataInput()
                user.name = fieldNama.text.toString()
                user.kia = dataKia
                var location = Location()
                location.district = dataKia.husband!!.districts
                location.subdistrict = dataKia.husband!!.sub_districts
                user.location = location

                userPref.setUser(user)
                finish()
            }
        }
        fieldTanggalLahir.setOnClickListener {
            callCalender("mother")
        }
        fieldTanggalLahirSuami.setOnClickListener {
            callCalender("husband")
        }
        fieldKabupaten.setOnClickListener {
            navigator.dataMaster(this, "kabupaten",1)
        }
        fieldKecamatan.setOnClickListener {
            navigator.dataMaster(this, "kecamatan",2)
        }
        fieldGoldarMom.setOnClickListener {
            navigator.dataMaster(this,"goldar",3)
        }
        fieldGoldarSuami.setOnClickListener {
            navigator.dataMaster(this,"goldar",4)
        }
        fieldPekerjaanMom.setOnClickListener {
            navigator.dataMaster(this, "pekerjaan",5)
        }
        fieldPendidikan.setOnClickListener {
            navigator.dataMaster(this, "pendidikan",6)
        }
        fieldAgamaMom.setOnClickListener {
            navigator.dataMaster(this, "agama",7)
        }
        fieldAgamaSuami.setOnClickListener {
            navigator.dataMaster(this,"agama",8)
        }

    }

    private fun isValid(): Boolean {
        if (fieldNomorKtp.text.toString().equals("")){
            fieldNomorKtp.setError("Wajib diisi!")
            return false
        } else if (fieldTempatLahir.text.toString().equals("")){
            fieldTempatLahir.setError("Wajib diisi!")
            return false
        } else if (fieldTanggalLahir.text.toString().equals("")){
            fieldTanggalLahir.setError("Wajib diisi!")
            return false
        } else if (fieldHamilKeberapa.text.toString().equals("")){
            fieldHamilKeberapa.setError("Wajib diisi!")
            return false
        } else if (fieldUmurAnak.text.toString().equals("")){
            fieldUmurAnak.setError("Isi 0 jika tidak ada")
            return false
        } else if (fieldPekerjaanMom.text.toString().equals("")){
            fieldPekerjaanMom.setError("Wajib diisi!")
            return false
        } else if (fieldAgamaMom.text.toString().equals("")){
            fieldAgamaMom.setError("Wajib diisi!")
            return false
        } else if (fieldNomorJkn.text.toString().equals("")){
            fieldNomorJkn.setError("Wajib diisi!")
            return false
        } else if (fieldGoldarMom.text.toString().equals("")){
            fieldGoldarMom.setError("Wajib diisi!")
            return false
        } else if (fieldNamaSuami.text.toString().equals("")){
            fieldNamaSuami.setError("Wajib diisi!")
            return false
        } else if (fieldTempatLahirSuami.text.toString().equals("")){
            fieldTempatLahirSuami.setError("Wajib diisi!")
            return false
        } else if (fieldTanggalLahirSuami.text.toString().equals("")){
            fieldTanggalLahirSuami.setError("Wajib diisi!")
            return false
        } else if (fieldNomorHpSuami.text.toString().equals("")){
            fieldNomorHpSuami.setError("Wajib diisi!")
            return false
        } else if (fieldAgamaSuami.text.toString().equals("")){
            fieldAgamaSuami.setError("Wajib diisi!")
            return false
        } else if (fieldGoldarSuami.text.toString().equals("")){
            fieldGoldarSuami.setError("Wajib diisi!")
            return false
        } else if (fieldAlamat.text.toString().equals("")){
            fieldAlamat.setError("Wajib diisi!")
            return false
        } else if (fieldKabupaten.text.toString().equals("")){
            fieldKabupaten.setError("Wajib diisi!")
            return false
        } else if (fieldKecamatan.text.toString().equals("")){
            fieldKecamatan.setError("Wajib diisi!")
            return false
        }
        return true
    }

    private fun setDataInput() {
        dataKia.nik = fieldNomorKtp.text.toString()
        dataKia.birth_place = fieldTempatLahir.text.toString()
        dataKia.birth_date = fieldTanggalLahir.text.toString()
        dataKia.number_of_pregnancy = fieldHamilKeberapa.text.toString()
        dataKia.last_child_age = fieldUmurAnak.text.toString()
        dataKia.last_education = fieldPendidikan.text.toString()
        dataKia.job = fieldPekerjaanMom.text.toString()
        dataKia.religion = fieldAgamaMom.text.toString()
        dataKia.jkn_number = fieldNomorJkn.text.toString()
        dataKia.blood_type = fieldGoldarMom.text.toString()
        var husband = Husband(fieldNamaSuami.text.toString(),fieldTempatLahirSuami.text.toString(),fieldTanggalLahirSuami.text.toString(),fieldNomorHpSuami.text.toString(),
        fieldAgamaSuami.text.toString(),fieldGoldarSuami.text.toString(),"","",fieldAlamat.text.toString(),fieldKabupaten.text.toString(),fieldKecamatan.text.toString())
        dataKia.husband = husband
    }

    private fun setData() {
        fieldNomorKtp.setText("${dataKia.nik}")
        fieldTempatLahir.setText("${dataKia.birth_place}")
        fieldTanggalLahir.setText("${dataKia.birth_date}")
        fieldHamilKeberapa.setText("${dataKia.number_of_pregnancy}")
        fieldUmurAnak.setText("${dataKia.last_child_age}")
        fieldPendidikan.setText("${dataKia.last_education}")
        fieldPekerjaanMom.setText("${dataKia.job}")
        fieldAgamaMom.setText("${dataKia.religion}")
        fieldNomorJkn.setText("${dataKia.jkn_number}")
        fieldGoldarMom.setText("${dataKia.blood_type}")
        if (dataKia.husband!=null){
            fieldNamaSuami.setText("${dataKia.husband!!.name}")
            fieldTempatLahirSuami.setText("${dataKia.husband!!.birth_place}")
            fieldTanggalLahirSuami.setText("${dataKia.husband!!.birth_date}")
            fieldNomorHpSuami.setText("${dataKia.husband!!.phone_number}")
            fieldAgamaSuami.setText("${dataKia.husband!!.religion}")
            fieldGoldarSuami.setText("${dataKia.husband!!.blood_type}")
            fieldAlamat.setText("${dataKia.husband!!.address}")
            fieldKabupaten.setText("${dataKia.husband!!.districts}")
            fieldKecamatan.setText("${dataKia.husband!!.sub_districts}")
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
                    if (keterangan.equals("mother")){
                        fieldTanggalLahir.setText(tanggal)
                    } else{
                        fieldTanggalLahirSuami.setText(tanggal)
                    }
                }, mYear, mMonth, mDay)

        datePickerDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldKabupaten.setText(dataMaster.name)
                id_kabupaten = dataMaster.id
            }
        } else if (requestCode==2){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldKecamatan.setText(dataMaster.name)
                id_kecamatan = dataMaster.id
            }
        } else if (requestCode==3){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldGoldarMom.setText(dataMaster.name)
            }
        } else if (requestCode==4){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldGoldarSuami.setText(dataMaster.name)
                id_kecamatan = dataMaster.id
            }
        } else if (requestCode==5){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldPekerjaanMom.setText(dataMaster.name)
            }
        } else if (requestCode==6){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldPendidikan.setText(dataMaster.name)
            }
        } else if (requestCode==7){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldAgamaMom.setText(dataMaster.name)
            }
        } else if (requestCode==8){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"),MasterListModel::class.java)
                fieldAgamaSuami.setText(dataMaster.name)
            }
        }
    }
}