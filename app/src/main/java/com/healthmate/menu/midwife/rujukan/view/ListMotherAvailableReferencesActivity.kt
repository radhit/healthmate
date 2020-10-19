package com.healthmate.menu.midwife.rujukan.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.artcak.starter.modules.reusable.adapter.PasienListAdapter
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.Fun
import com.healthmate.commons.helper.EndlessScrollListener
import com.healthmate.di.injector
import com.healthmate.menu.midwife.pasien.data.PasienViewModel
import com.healthmate.menu.midwife.pasien.view.MainPasienMidwifeActivity
import com.healthmate.menu.reusable.data.User
import com.healthmate.menu.reusable.view.ListLocationActivity
import kotlinx.android.synthetic.main.activity_list_location.*
import kotlinx.android.synthetic.main.activity_list_mother_available_references.*
import kotlinx.android.synthetic.main.activity_list_mother_available_references.btn_cari
import kotlinx.android.synthetic.main.activity_list_mother_available_references.fieldSearch
import kotlinx.android.synthetic.main.activity_list_mother_available_references.rv_list
import kotlinx.android.synthetic.main.activity_list_mother_available_references.tv_loading

class ListMotherAvailableReferencesActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            val intent = Intent(activity, ListMotherAvailableReferencesActivity::class.java)
            return intent
        }
    }

    lateinit var pasienListAdapter: PasienListAdapter
    var listPasien: ArrayList<User> = ArrayList()
    var listPasienFull: ArrayList<User> = ArrayList()
    var cursor: String = ""
    var search: Boolean = false

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.pasienVM()).get(PasienViewModel::class.java)
    }

    override fun getView(): Int = R.layout.activity_list_mother_available_references

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setRecycleView()
        getData("awal")
        fieldSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().equals("")){
                    if (search){
                        search = false
                        pasienListAdapter.lists.clear()
                        pasienListAdapter.lists.addAll(listPasienFull)
                        pasienListAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
        btn_cari.setOnClickListener {
            if (!fieldSearch.text.toString().equals("")){
                search = true
                search()
            }
        }
    }

    private fun getData(keterangan: String) {
        val payload = Payload()
        if (keterangan.equals("load")){
            payload.url = "${Urls.registerMidwife}/${userPref.getUser().id}/mothers&cursor=${cursor}"
        } else{
            payload.url = "${Urls.registerMidwife}/${userPref.getUser().id}/mothers"
        }
        viewModel.listMother(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            showLoadingDialog()
                        }
                        Result.Status.SUCCESS->{
                            closeLoadingDialog()
                            cursor = result.cursor
                            if (keterangan.equals("load")){
                                if (result.data!!.size>0){
                                    for (data in result.data!!){
                                        pasienListAdapter.lists.add(data)
                                    }
                                    listPasien.clear()
                                    listPasien.addAll(pasienListAdapter.lists)
                                }
                                pasienListAdapter.notifyDataSetChanged()
                            } else{
                                pasienListAdapter.lists.clear()
                                pasienListAdapter.lists.addAll(result.data!!)
                                listPasien.clear()
                                listPasien.addAll(pasienListAdapter.lists)
                                pasienListAdapter.notifyDataSetChanged()
                                if (pasienListAdapter.lists.size>0){
                                    rv_list.visibility = View.VISIBLE
                                    tv_loading.visibility = View.GONE
                                } else{
                                    rv_list.visibility = View.GONE
                                    tv_loading.visibility = View.VISIBLE
                                    tv_loading.text = "Data Kosong"
                                }
                            }
                            listPasienFull = pasienListAdapter.lists
                        }
                        Result.Status.ERROR->{
                            tv_loading.visibility = View.VISIBLE
                            rv_list.visibility = View.GONE
                            tv_loading.text = "Data Kosong"
                            if (result.response_code==401){
                                result.message = "Token expired"
                            }
                            Fun.handleError(this,result)
                        }
                    }
                })
    }

    fun setRecycleView(){
        pasienListAdapter = PasienListAdapter(this)
        rv_list.adapter = pasienListAdapter
        initiateLinearLayoutRecyclerView(rv_list,object : RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {
//                navigator.mainDetilPasien(this,gson.toJson(pasienListAdapter.lists[position]))
                navigator.formRujukan(this@ListMotherAvailableReferencesActivity,gson.toJson(listPasien[position]),"create")
//                val intent = Intent()
//                intent.putExtra("data",gson.toJson(pasienListAdapter.lists.get(position)))
//                setResult(Activity.RESULT_OK,intent)
                finish()
            }
            override fun onLongClick(view: View, position: Int) {
            }
        })
        rv_list.addOnScrollListener(
                object : EndlessScrollListener(){
                    override fun onLoadMore() {
                        if (listPasien.size>19){
                            println("addOnScrollListener nextPage")
                            getData("load")
                        }
                    }

                }
        )
    }

    private fun search() {
        val payload = Payload()
        payload.url = "${Urls.registerMidwife}/${userPref.getUser().id}/mothers?num=500&name=${fieldSearch.text.toString()}"
        viewModel.listMother(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            showLoadingDialog()
                            tv_loading.visibility = View.GONE
                            rv_list.visibility = View.GONE
                        }
                        Result.Status.SUCCESS->{
                            closeLoadingDialog()
                            pasienListAdapter.lists.clear()
                            pasienListAdapter.lists.addAll(result.data!!)
                            listPasien.clear()
                            listPasien.addAll(pasienListAdapter.lists)

                            pasienListAdapter.notifyDataSetChanged()
                            if (pasienListAdapter.lists.size>0){
                                rv_list.visibility = View.VISIBLE
                                tv_loading.visibility = View.GONE
                            } else{
                                rv_list.visibility = View.GONE
                                tv_loading.visibility = View.VISIBLE
                                tv_loading.text = "Data Kosong"
                            }
                        }
                        Result.Status.ERROR->{
                            closeLoadingDialog()
                            tv_loading.visibility = View.VISIBLE
                            rv_list.visibility = View.GONE
                            tv_loading.text = "Data Kosong"
                            if (result.response_code==401){
                                result.message = "Token expired"
                            }
                            Fun.handleError(this,result)
                        }
                    }
                })
    }
}