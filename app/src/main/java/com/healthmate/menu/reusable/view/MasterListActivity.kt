package com.healthmate.menu.reusable.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.artcak.starter.modules.reusable.adapter.MasterListAdapter
import com.healthmate.R
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.functions.setOnClick
import com.healthmate.menu.mom.kia.view.MainKiaActivity
import com.healthmate.menu.reusable.data.MasterListModel
import kotlinx.android.synthetic.main.activity_master_list.*

class MasterListActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity,keterangan: String): Intent {
            val intent = Intent(activity, MasterListActivity::class.java)
            intent.putExtra(EXTRA,keterangan)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_master_list
    var adapter: MasterListAdapter = MasterListAdapter()
    var list: ArrayList<MasterListModel> = ArrayList()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Daftar ${intent.getStringExtra(EXTRA)}")
        fieldKeyword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.lists.clear()
                if (p0.toString().equals("")){
                    adapter.lists.addAll(list)
                }else{
                    for (data in list){
                        if (data.name.toLowerCase().contains(p0.toString().toLowerCase())){
                            adapter.lists.add(data)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }
        })
        setRecycleView()
        if (intent.getStringExtra(EXTRA).equals("kecamatan")){
            getKecamatan()
        } else if (intent.getStringExtra(EXTRA).equals("kabupaten")){
            getKabupaten()
        } else if (intent.getStringExtra(EXTRA).equals("goldar")){
            getGoldar()
        } else if (intent.getStringExtra(EXTRA).equals("agama")){
            getAgama()
        } else if (intent.getStringExtra(EXTRA).equals("pekerjaan")){
            getPekerjaan()
        } else if (intent.getStringExtra(EXTRA).equals("pendidikan")){
            getPendidikan()
        } else if (intent.getStringExtra(EXTRA).equals("hospital")){
            getHospital()
        }
    }

    private fun getHospital() {
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","RS Mitra Bangsa"))
        arrays.add(MasterListModel("2","RS Jiwa Sejahtera"))
        arrays.add(MasterListModel("3","RS Bakti Sembuh"))
        arrays.add(MasterListModel("4","Puskesmas Keputih"))
        arrays.add(MasterListModel("5","BPM Sri Mulyani"))
        arrays.add(MasterListModel("6","BPM Suharti"))

        adapter.updateLists(arrays)
    }

    private fun getPendidikan() {
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","TK"))
        arrays.add(MasterListModel("2","SD"))
        arrays.add(MasterListModel("3","SMP"))
        arrays.add(MasterListModel("4","SMA"))
        arrays.add(MasterListModel("5","Kuliah"))
        arrays.add(MasterListModel("6","Tidak Sekolah"))

        adapter.updateLists(arrays)
    }

    private fun getPekerjaan() {
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","PNS"))
        arrays.add(MasterListModel("2","Wiraswasta"))
        arrays.add(MasterListModel("3","Ibu Rumah Tangga"))

        adapter.updateLists(arrays)
    }

    private fun getAgama() {
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","Islam"))
        arrays.add(MasterListModel("2","Kristen"))
        arrays.add(MasterListModel("3","Katolik"))
        arrays.add(MasterListModel("4","Hindu"))
        arrays.add(MasterListModel("5","Budha"))

        adapter.updateLists(arrays)

    }

    private fun getGoldar() {
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","A"))
        arrays.add(MasterListModel("2","B"))
        arrays.add(MasterListModel("3","AB"))
        arrays.add(MasterListModel("4","O"))

        adapter.updateLists(arrays)

    }

    private fun getKabupaten() {
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","Kota Surabaya"))
        arrays.add(MasterListModel("2","Kota Batu"))
        arrays.add(MasterListModel("3","Kota Malang"))
        arrays.add(MasterListModel("4","Kabupaten Sampang"))
        arrays.add(MasterListModel("5","Kabupaten Probolinggo"))

        adapter.updateLists(arrays)
    }

    private fun getKecamatan() {
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","Sukolilo"))
        arrays.add(MasterListModel("2","Gunung Anyar"))
        arrays.add(MasterListModel("3","Benowo"))
        arrays.add(MasterListModel("4","Kenjeran"))
        arrays.add(MasterListModel("5","Gubeng"))

        adapter.updateLists(arrays)

    }

    private fun setRecycleView() {
        rv_list.adapter = adapter
        rv_list.setOnClick(object : RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent()
                intent.putExtra("data",gson.toJson(adapter.lists.get(position)))
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
            override fun onLongClick(view: View, position: Int) {

            }
        })
    }
}