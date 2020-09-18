package com.healthmate.menu.mom.rapor.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.base.BaseFragment
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.Fun
import com.healthmate.di.injector
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.healthmate.commons.utils.AxisDateFormatter
import com.healthmate.menu.mom.rapor.data.AncModel
import com.healthmate.menu.reusable.data.MasterViewModel
import kotlinx.android.synthetic.main.fragment_grafik_bb.*
import kotlinx.android.synthetic.main.fragment_list_rapor.tv_loading

class GrafikBbFragment : BaseFragment() {

    override fun getViewId(): Int = R.layout.fragment_grafik_bb
    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.masterVM()).get(MasterViewModel::class.java)
    }
    var dataAnc: AncModel = AncModel()
    var dataAncs: ArrayList<AncModel> = ArrayList()

    companion object {
        fun newInstance(): GrafikBbFragment = GrafikBbFragment()
    }

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        getData()
    }

    private fun getData() {
        val payload = Payload()
        payload.url = "${Urls.ancsMom}?mother_id=${userPref.getUser().id}&num=1"
        viewModel.getAncs(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            tv_loading.visibility = View.VISIBLE
                            ll_data.visibility = View.GONE
                        }
                        Result.Status.SUCCESS->{
                            if (result.data!!.size>0){
                                tv_loading.visibility = View.GONE
                                ll_data.visibility = View.VISIBLE
                                dataAncs.addAll(result.data!!)
                                dataAnc = result.data!![0]
                                setData()
                            } else{
                                tv_loading.visibility = View.VISIBLE
                                ll_data.visibility = View.GONE
                                tv_loading.text = "Data Kosong"
                            }
                        }
                        Result.Status.ERROR->{
                            tv_loading.visibility = View.VISIBLE
                            ll_data.visibility = View.GONE
                            tv_loading.text = "Data Kosong"
                            Fun.handleError(activity!!,result)
                        }
                    }
                })
    }

    private fun setData() {
        tv_bb_awal.text = "${dataAnc.initial_weight} Kg"
        tv_minggu_ke.text = dataAnc.week_of_pregnancy
        tv_bb_now.text = "${dataAnc.weight} Kg"
        tv_perubahan_bb.text = "${dataAnc.differenced_weight} Kg"
        tv_suggested.text = "Suggested Weight\n${dataAnc.suggested_weight} Kg"
        setGrafik()
    }

    private fun setGrafik() {
        val data = ArrayList<Entry>()
        var floatNumber: String
        var floatValue: String
        var date = ArrayList<String>()
        for (position in 0..dataAncs.size-1){
            floatNumber = "${position}F"
            floatValue = "${dataAncs[position].weight}F"
            data.add(Entry(floatNumber.toFloat(),floatValue.toFloat()))
            date.add(dataAncs[position].created_time.split("T")[0])
        }

        val lineBB = LineDataSet(data, "Berat Badan")
        lineBB.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineBB.color = resources.getColor(R.color.colorPrimary)
        lineBB.circleRadius = 5f
        lineBB.setCircleColor(resources.getColor(R.color.colorPrimary))

        lineChart.legend.isEnabled = false
        lineChart.description.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.data = LineData(lineBB)
        lineChart.animateXY(100, 500)
        val tanggal = AxisDateFormatter(date.toArray(arrayOfNulls<String>(date.size)))
        lineChart.xAxis?.setValueFormatter(tanggal)
    }
}