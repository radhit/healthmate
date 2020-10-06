package com.healthmate.menu.reusable.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.menu.reusable.adapter.Selectable.SelectableAdapter
import com.healthmate.menu.reusable.adapter.Selectable.SelectableItem
import com.healthmate.menu.reusable.adapter.Selectable.SelectableViewHolder
import com.healthmate.menu.reusable.data.Item
import kotlinx.android.synthetic.main.activity_list_choosed_item.*

class ListChoosedItemActivity : BaseActivity(), SelectableViewHolder.OnItemSelectedListener {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, keterangan: String = ""): Intent {
            val intent = Intent(activity, ListChoosedItemActivity::class.java)
            intent.putExtra(EXTRA, keterangan)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_list_choosed_item
    lateinit var selectableAdapter : SelectableAdapter
    var list: ArrayList<Item> = arrayListOf()
    var selectedItems: ArrayList<Item> = arrayListOf()
    var selectedItemsRaw: ArrayList<Item> = arrayListOf()
    var data: ArrayList<String> = arrayListOf()
    var status = true


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("${intent.getStringExtra(EXTRA)}")
        if (intent.getStringExtra(EXTRA).equals("Riwayat Penyakit")){
            setData()
        } else{
            setDataKeluhan()
        }
        setRecycleView()
        btn_simpan.setOnClickListener {
            if (selectedItems.size>0){
                data.clear()
                for (i in 0..selectedItems.size-1){
                    data.add(selectedItems[i].name)
                }
                val intent = Intent()
                intent.putExtra("data", gson.toJson(data))
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else{
                createDialog("Anda harus memilih salah satu jawaban")
            }
        }
    }

    private fun setDataKeluhan() {
        val arrays: ArrayList<Item> = arrayListOf()
        arrays.add(Item("Tidak ada keluhan"))
        arrays.add(Item("Mual/Muntah"))
        arrays.add(Item("Sesak"))
        arrays.add(Item("Pusing"))
        arrays.add(Item("Nyeri kepala"))
        arrays.add(Item("Mata berkunang-kunang"))
        arrays.add(Item("Perdarahan"))
        arrays.add(Item("Keluar air (merembes air)"))
        arrays.add(Item("Perut kenceng-kenceng/kontraksi rahim"))
        arrays.add(Item("Gerak anak berkurang"))
        arrays.add(Item("Gerak anak tidak dirasakan"))
        arrays.add(Item("Lainnya"))

        list.clear()
        list.addAll(arrays)
    }

    private fun setData() {
        val arrays: ArrayList<Item> = arrayListOf()
        arrays.add(Item("Sakit Jantung"))
        arrays.add(Item("Sakit paru-paru/TBC"))
        arrays.add(Item("Sakit Liver/Hepatitis"))
        arrays.add(Item("Sakit Ginjal/Gagal Ginjal"))
        arrays.add(Item("Sakit nyeri/sakit kepala berat"))
        arrays.add(Item("Sakit tekanan darah tinggi/Hipertensi"))
        arrays.add(Item("Sakit batuk lama"))
        arrays.add(Item("Sakit sesak/Asma"))
        arrays.add(Item("Sakit kencing manis (Diabetes)"))
        arrays.add(Item("Sakit kelenjar tiroid/gondoken"))
        arrays.add(Item("Sakit Malaria"))
        arrays.add(Item("Sakit HIV"))
        arrays.add(Item("Sakit Infeksi Menular Seksual"))
        arrays.add(Item("Riwayat Kejang saat hamil/melahirkan/nifas"))
        arrays.add(Item("Penyakit gangguan pembekuan darah/Thalasemia/Hemofilia"))
        arrays.add(Item("Riwayat operasi saat melahirkan"))
        arrays.add(Item("Riwayat operasi tumor kandungan"))
        arrays.add(Item("Plasenta Previa"))
        arrays.add(Item("Anemia Refrakter"))
        arrays.add(Item("Tidak ada"))
        arrays.add(Item("Lainnya"))

        list.clear()
        list.addAll(arrays)
    }

    private fun setRecycleView() {
        selectableAdapter = SelectableAdapter(this, list, true)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        rv_list.setLayoutManager(mLayoutManager)
        rv_list.setItemAnimator(DefaultItemAnimator())
        rv_list.setAdapter(selectableAdapter)
//        rv_list.addOnItemTouchListener(RecyclerViewTouchListener(baseContext, rv_list, object : RecyclerViewClickListener {
//            override fun onClick(view: View, position: Int) {
//
//            }
//
//            override fun onLongClick(view: View, position: Int) {
//
//            }
//        }))
        selectableAdapter.notifyDataSetChanged()
    }

    override fun onItemSelected(item: SelectableItem?) {
        selectedItemsRaw = selectableAdapter.selectedItems
        if (!status) {
            selectedItems.clear()
        }
        for (i in selectedItemsRaw.indices) {
            var flag = true
            if (selectedItems.size == 0) {
                selectedItems.add(selectedItemsRaw[i])
            } else {
                for (j in selectedItems.indices) {
                    if (selectedItemsRaw[i].name.equals(selectedItems[j].name)) {
                        flag = false
                        break
                    }
                }
                if (flag) {
                    selectedItems.add(selectedItemsRaw[i])
                }
            }
        }
    }
}