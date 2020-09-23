package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.menu.midwife.pasien.data.UmpanBalikRujukan
import kotlinx.android.synthetic.main.activity_form_umpan_balik_rujukan.*
import java.util.*

class FormUmpanBalikRujukanActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String): Intent {
            val intent = Intent(activity, FormRujukanActivity::class.java)
            intent.putExtra(EXTRA,data)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_form_umpan_balik_rujukan
    var umpanBalikRujukan: UmpanBalikRujukan = UmpanBalikRujukan()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        fieldTanggal.setOnClickListener {
            callCalender()
        }
        fieldWaktu.setOnClickListener {
            showTimePicker()
        }
        btn_simpan.setOnClickListener {
            if (isValid()){
                setDataInput()
            }
        }
    }

    private fun setDataInput() {
        umpanBalikRujukan.date = "${fieldTanggal.text.toString()}T01:00:00+07:00"
        umpanBalikRujukan.time = fieldWaktu.text.toString()
        umpanBalikRujukan.name = fieldPenerimaRujukan.text.toString()
        umpanBalikRujukan.dx = fieldDx.text.toString()
        umpanBalikRujukan.advice = fieldAnjuran.text.toString()
        umpanBalikRujukan.action = fieldTindakan.text.toString()
    }

    private fun isValid(): Boolean {
        if (fieldTanggal.text.toString().equals("")){
            fieldTanggal.setError("Wajib diisi")
            return false
        } else if (fieldWaktu.text.toString().equals("")){
            fieldWaktu.setError("Wajib diisi")
            return false
        } else if (fieldPenerimaRujukan.text.toString().equals("")){
            fieldPenerimaRujukan.setError("Wajib diisi")
            return false
        } else if (fieldAnjuran.text.toString().equals("")){
            fieldAnjuran.setError("Wajib diisi")
            return false
        } else if (fieldDx.text.toString().equals("")){
            fieldDx.setError("Wajib diisi")
            return false
        } else if (fieldTindakan.text.toString().equals("")){
            fieldTindakan.setError("Wajib diisi")
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
}