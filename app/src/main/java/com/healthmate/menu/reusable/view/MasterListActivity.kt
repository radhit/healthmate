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
        } else if (intent.getStringExtra(EXTRA).equals("rhesus") || intent.getStringExtra(EXTRA).equals("bta") || intent.getStringExtra(EXTRA).equals("malaria") || intent.getStringExtra(EXTRA).equals("sifilis")){
            getPlusMin()
        } else if (intent.getStringExtra(EXTRA).equals("hepatitis") || intent.getStringExtra(EXTRA).equals("hiv")){
            getHepatitis()
        } else if (intent.getStringExtra(EXTRA).equals("penolong persalinan")){
            getPenolongPersalinan()
        } else if (intent.getStringExtra(EXTRA).equals("dana persalinan")){
            getDanaPersalinan()
        } else if (intent.getStringExtra(EXTRA).equals("kendaraan")){
            getKendaraan()
        } else if (intent.getStringExtra(EXTRA).equals("metode kb")){
            getMetodeKb()
        } else if (intent.getStringExtra(EXTRA).equals("kontrasepsi")){
            getKontrasepsi()
        } else if (intent.getStringExtra(EXTRA).equals("imunisasi")){
            getImunisasi()
        } else if (intent.getStringExtra(EXTRA).equals("cara persalinan")){
            getCaraPersalinan()
        } else if (intent.getStringExtra(EXTRA).equals("kaki bengkak")){
            getKakiBengkak()
        } else if (intent.getStringExtra(EXTRA).equals("protein urin") || intent.getStringExtra(EXTRA).equals("reduksi urin")){
            getUrin()
        } else if (intent.getStringExtra(EXTRA).equals("letak janin")){
            getLetakJanin()
        }
    }

    private fun getLetakJanin() {
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","Letak Kepala"))
        arrays.add(MasterListModel("2","Letak Sungsang"))
        arrays.add(MasterListModel("3","Letak Lintang"))
        arrays.add(MasterListModel("4","Lainnya"))

        adapter.updateLists(arrays)
    }


    private fun getUrin(){
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","Negatif"))
        arrays.add(MasterListModel("2","+/-"))
        arrays.add(MasterListModel("3","+1"))
        arrays.add(MasterListModel("4","+2"))
        arrays.add(MasterListModel("5","+3"))
        arrays.add(MasterListModel("6","+4"))

        adapter.updateLists(arrays)
    }

    private fun getKakiBengkak(){
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","Tidak Bengkak"))
        arrays.add(MasterListModel("2","+1"))
        arrays.add(MasterListModel("3","+2"))
        arrays.add(MasterListModel("4","+3"))

        adapter.updateLists(arrays)
    }

    private fun getCaraPersalinan(){
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","Normal Pervaginam"))
        arrays.add(MasterListModel("2","Sungsang Pervaginam"))
        arrays.add(MasterListModel("3","Ekstraksi vakum"))
        arrays.add(MasterListModel("4","Tarikan cunam/forseps"))
        arrays.add(MasterListModel("5","Operasi sesar/CS"))
        arrays.add(MasterListModel("6","Laparatomi"))
        arrays.add(MasterListModel("7","Lainnya"))
        arrays.add(MasterListModel("8","Manual plasenta"))
        arrays.add(MasterListModel("9","Riwayat infus/transfusi"))
        arrays.add(MasterListModel("10","Lainnya"))

        adapter.updateLists(arrays)
    }

    private fun getImunisasi(){
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","T1"))
        arrays.add(MasterListModel("2","T2"))
        arrays.add(MasterListModel("3","T3"))
        arrays.add(MasterListModel("4","T4"))
        arrays.add(MasterListModel("5","T5"))
        adapter.updateLists(arrays)
    }

    private fun getKontrasepsi(){
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","Tidak KB"))
        arrays.add(MasterListModel("2","IUD/Spiral"))
        arrays.add(MasterListModel("3","KB Suntik 3 bulanan"))
        arrays.add(MasterListModel("4","KB Suntik 1 bulanan"))
        arrays.add(MasterListModel("5","KB Susuk"))
        arrays.add(MasterListModel("6","KB Pil"))
        arrays.add(MasterListModel("7","KB Alami"))
        arrays.add(MasterListModel("8","MOW"))
        arrays.add(MasterListModel("9","Suami MOP"))
        arrays.add(MasterListModel("10","Lainnya"))
        adapter.updateLists(arrays)
    }

    private fun getMetodeKb(){
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","Tidak KB"))
        arrays.add(MasterListModel("2","IUD/Spiral"))
        arrays.add(MasterListModel("3","KB Suntik 3 bulanan"))
        arrays.add(MasterListModel("4","KB Suntik 1 bulanan"))
        arrays.add(MasterListModel("5","KB Susuk"))
        arrays.add(MasterListModel("6","KB Pil"))
        arrays.add(MasterListModel("7","KB Mandiri"))
        adapter.updateLists(arrays)
    }


    private fun getKendaraan(){
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","Kendaraan Pribadi"))
        arrays.add(MasterListModel("2","Ambulan Desa"))
        arrays.add(MasterListModel("3","Kendaraan Umum"))
        adapter.updateLists(arrays)
    }

    private fun getDanaPersalinan(){
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","Biaya Sendiri"))
        arrays.add(MasterListModel("2","BPJS-Kes"))
        arrays.add(MasterListModel("3","Pemda/desa setempat"))
        arrays.add(MasterListModel("4","Keluarga"))
        arrays.add(MasterListModel("5","Asuransi Lainnya"))
        adapter.updateLists(arrays)
    }

    private fun getPenolongPersalinan(){
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","Bidan"))
        arrays.add(MasterListModel("2","Dokter Umum"))
        arrays.add(MasterListModel("3","Dokter SpOG"))
        arrays.add(MasterListModel("4","Dukun"))
        arrays.add(MasterListModel("5","Sendiri/Brojol"))
        arrays.add(MasterListModel("6","Lainnya"))
        adapter.updateLists(arrays)
    }

    private fun getHepatitis() {
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","R/+"))
        arrays.add(MasterListModel("2","NR/-"))

        adapter.updateLists(arrays)
    }

    private fun getPlusMin() {
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","+"))
        arrays.add(MasterListModel("2","-"))

        adapter.updateLists(arrays)
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
        arrays.add(MasterListModel("1","Tidak Sekolah"))
        arrays.add(MasterListModel("2","SD"))
        arrays.add(MasterListModel("3","SMP"))
        arrays.add(MasterListModel("4","SMA"))
        arrays.add(MasterListModel("5","Akademi"))
        arrays.add(MasterListModel("6","Perguruan Tinggi"))

        adapter.updateLists(arrays)
    }

    private fun getPekerjaan() {
        val arrays: ArrayList<MasterListModel> = arrayListOf()
        arrays.add(MasterListModel("1","Tidak Bekerja"))
        arrays.add(MasterListModel("2","Wiraswasta"))
        arrays.add(MasterListModel("3","Karyawan Swasta"))
        arrays.add(MasterListModel("4","Karyawan BUMN/BUMD"))
        arrays.add(MasterListModel("5","ASN/PNS"))
        arrays.add(MasterListModel("6","Anggota TNI"))
        arrays.add(MasterListModel("7","Anggota POLRI"))
        arrays.add(MasterListModel("8","Lainnya"))

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