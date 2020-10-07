package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.artcak.starter.modules.reusable.adapter.AncListAdapter
import com.artcak.starter.modules.reusable.adapter.KalaListAdapter
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.commons.helper.EndlessScrollListener
import com.healthmate.di.injector
import com.healthmate.menu.midwife.pasien.data.IncKalaModel
import com.healthmate.menu.midwife.pasien.data.PasienViewModel
import com.healthmate.menu.mom.rapor.data.AncModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_main_kala.*
import kotlinx.android.synthetic.main.activity_main_kala.btn_pemeriksaan
import kotlinx.android.synthetic.main.activity_main_kala.rv_list
import kotlinx.android.synthetic.main.activity_main_kala.tv_loading
import kotlinx.android.synthetic.main.fragment_data_anc.*

class MainKalaActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        val EXTRA_TYPE = "EXTRA_TYPE"
        @JvmStatic
        fun getCallingIntent(activity: Activity, type: String, data: String): Intent {
            val intent = Intent(activity, MainKalaActivity::class.java)
            intent.putExtra(FormInputKalaActivity.EXTRA_TYPE, type)
            intent.putExtra(EXTRA, data)
            intent.putExtra(EXTRA_TYPE, type)
            return intent
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.pasienVM()).get(PasienViewModel::class.java)
    }

    override fun getView(): Int = R.layout.activity_main_kala
    var dataMother: User = User()
    var type: String = ""
    var kala_number = 1
    lateinit var adapter: KalaListAdapter
    var listKala: ArrayList<IncKalaModel> = ArrayList()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        type = intent.getStringExtra(EXTRA_TYPE)
        dataMother = gson.fromJson(intent.getStringExtra(EXTRA),User::class.java)
        this.setTitle("Data ${type}")
        if (type.contains("1")){
            kala_number = 1
        } else if (type.contains("2")){
            kala_number = 2
        } else if (type.contains("3")){
            kala_number = 3
        } else if (type.contains("4")){
            kala_number = 4
        }

        btn_pemeriksaan.setOnClickListener {
            if (kala_number==1){
                navigator.formKalaInc(this,type,gson.toJson(dataMother))
            } else if (kala_number==2){
                navigator.formKala2Inc(this,type,gson.toJson(dataMother))
            } else if (kala_number==3){
                navigator.formKala3Inc(this,type,gson.toJson(dataMother))
            } else if (kala_number==4){
                navigator.formKala4Inc(this,type,gson.toJson(dataMother))
            }
        }
        setRecycleView()
        getData()
    }

    fun setRecycleView(){
        adapter = KalaListAdapter(this)
        rv_list.adapter = adapter
        initiateLinearLayoutRecyclerView(rv_list,object : RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {

            }
            override fun onLongClick(view: View, position: Int) {
            }
        })

    }

    private fun getData() {
        val payload = Payload()
        payload.url = "${Urls.registerMother}/${dataMother.id}/incs/kala/${kala_number}"
        viewModel.getDataKala(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            showLoadingDialog()
                            rv_list.visibility = View.GONE
                        }
                        Result.Status.SUCCESS->{
                            closeLoadingDialog()
                            var data = result.data!!
                            if (data.size>0){
                                rv_list.visibility = View.VISIBLE
                                adapter.lists.clear()
                                adapter.lists.addAll(result.data!!)
                                listKala.clear()
                                listKala.addAll(adapter.lists)
                                adapter.notifyDataSetChanged()

                            } else{
                                tv_loading.text = "Data Kosong"
                                tv_loading.visibility = View.VISIBLE
                                rv_list.visibility = View.GONE
                            }
                        }
                        Result.Status.ERROR->{
                            closeLoadingDialog()
                            tv_loading.visibility = View.VISIBLE
                            rv_list.visibility = View.GONE
                            tv_loading.text = "Data Kosong"
                            createDialog(result.message!!)
                        }
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}