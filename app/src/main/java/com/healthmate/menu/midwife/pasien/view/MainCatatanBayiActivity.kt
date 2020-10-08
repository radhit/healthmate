package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.replaceEmpty
import com.healthmate.di.injector
import com.healthmate.menu.midwife.pasien.data.BabyNote
import com.healthmate.menu.midwife.pasien.data.PasienViewModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_main_catatan_bayi.*

class MainCatatanBayiActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String): Intent {
            val intent = Intent(activity, MainCatatanBayiActivity::class.java)
            intent.putExtra(EXTRA,data)
            return intent
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.pasienVM()).get(PasienViewModel::class.java)
    }
    override fun getView(): Int = R.layout.activity_main_catatan_bayi
    var dataMother: User = User()
    var babyNote: BabyNote = BabyNote()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Catatan Bayi")
        dataMother = gson.fromJson(intent.getStringExtra(MainRingkasanPersalinanActivity.EXTRA),User::class.java)
        getData()
        btn_edit.setOnClickListener {
            navigator.formBabyNoteInc(this, gson.toJson(babyNote))
        }
    }

    private fun getData() {
        val payload = Payload()
        payload.url = "${Urls.registerMother}/${dataMother.id}/incs/baby-note"
        viewModel.getDataBabyNote(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            showLoadingDialog()
                        }
                        Result.Status.SUCCESS->{
                            closeLoadingDialog()
                            babyNote = result.data!!
                            setData()
                        }
                        Result.Status.ERROR->{
                            closeLoadingDialog()
                            createDialog(result.message!!)
                        }
                    }
                })
    }

    private fun setData() {
        tv_anak_ke.text = babyNote.child_number.toString()
        tv_berat.text = "${babyNote.weight} gram"
        tv_panjang.text = "${babyNote.height} cm"
        tv_lingkar_kepala.text = "${babyNote.lila} cm"
        tv_jenis_kelamin.text = "${babyNote.gender.replaceEmpty("-")}"
        tv_keadaan_lahir.text = babyNote.baby_condition.replaceEmpty("-")
        tv_skor.text = babyNote.apgar.toString()
        var asuhan = ""
        for (i in 0..babyNote.asuhan_bayi!!.size-1){
            asuhan = "${asuhan}${babyNote.asuhan_bayi!![i]}\n"
        }
        tv_asuhan.text = asuhan.replaceEmpty("-")
        tv_keterangan.text = babyNote.information.replaceEmpty("-")
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}