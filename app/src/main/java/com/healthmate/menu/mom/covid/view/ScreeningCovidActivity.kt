package com.healthmate.menu.mom.covid.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.healthmate.R
import com.healthmate.api.DataResponse
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.menu.mom.covid.data.ScreeningCovidAnswer
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_screening_covid.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScreeningCovidActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            val intent = Intent(activity, ScreeningCovidActivity::class.java)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_screening_covid
    var currentQuetion: Int = 1
    var question3: String = ""
    var screeningCovidAnswer: ScreeningCovidAnswer = ScreeningCovidAnswer()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        tv_mulai.setOnClickListener {
            createDialog("Pastikan anda menjawab dengan benar!",{
                ll_awal.visibility = View.GONE
                rl_pertanyaan.visibility = View.VISIBLE
            })
        }
        btn_ya.setOnClickListener {
            if (currentQuetion<6){
                saveAnswer(true)
                currentQuetion+=1
                changeQuetion()
            } else{
                saveAnswer(true)
                updateData()
            }
        }
        btn_tidak.setOnClickListener {
            if (currentQuetion<6){
                saveAnswer(false)
                currentQuetion+=1
                changeQuetion()
            } else{
                saveAnswer(false)
                updateData()
            }
        }

    }

    fun updateData(){
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(screeningCovidAnswer))
        val call: Call<DataResponse<User>> = baseApi.changeCovidStatus("${Urls.registerMother}/${userPref.getUser().id}/covid_status",requestBody)
        call.enqueue(object : Callback<DataResponse<User>> {
            override fun onResponse(call: Call<DataResponse<User>>?, response: Response<DataResponse<User>>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299){
                        var user = response.body()!!.data
                        user!!.token = userPref.getUser().token
                        userPref.setUser(user!!)
                        finish()
                    } else{
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@ScreeningCovidActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<User>>?, t: Throwable?) {
                Toast.makeText(this@ScreeningCovidActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun saveAnswer(jawaban: Boolean) {
        if (currentQuetion==1){
            screeningCovidAnswer.finished_quarantine = jawaban
        } else if (currentQuetion==2){
            screeningCovidAnswer.heavy_breath = jawaban
        } else if (currentQuetion==3){
            if (question3.equals("have_pcr")){
                screeningCovidAnswer.have_pcr = jawaban
            } else{
                screeningCovidAnswer.sore_throat = jawaban
            }
        } else if (currentQuetion==4){
            screeningCovidAnswer.going_to_covid_area = jawaban
        } else if (currentQuetion==5){
            screeningCovidAnswer.contact_with_covid = jawaban
        } else if (currentQuetion==6){
            screeningCovidAnswer.medical_without_apd = jawaban
        }
    }

    private fun changeQuetion() {
        if (currentQuetion==2){
            tv_pertanyaan.text = resources.getString(R.string.pertanyaan_covid_2)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.pertanyaan_2)).into(iv_pertanyaan)
        } else if (currentQuetion==3){
            if (screeningCovidAnswer.heavy_breath){
                tv_pertanyaan.text = resources.getString(R.string.pertanyaan_covid_3a)
                question3 = "have_pcr"
            } else{
                tv_pertanyaan.text = resources.getString(R.string.pertanyaan_covid_3b)
                question3 = "sore_throat"
            }
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.pertanyaan_3)).into(iv_pertanyaan)
        } else if (currentQuetion==4){
            tv_pertanyaan.text = resources.getString(R.string.pertanyaan_covid_4)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.pertanyaan_4)).into(iv_pertanyaan)
        } else if (currentQuetion==5){
            tv_pertanyaan.text = resources.getString(R.string.pertanyaan_covid_5)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.pertanyaan_5)).into(iv_pertanyaan)
        } else if (currentQuetion==6){
            tv_pertanyaan.text = resources.getString(R.string.pertanyaan_covid_6)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.apd)).into(iv_pertanyaan)
        }
    }
}