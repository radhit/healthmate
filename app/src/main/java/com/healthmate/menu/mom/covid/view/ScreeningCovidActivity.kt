package com.healthmate.menu.mom.covid.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.healthmate.R
import com.healthmate.api.DataResponse
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.menu.auth.view.SigninActivity
import com.healthmate.menu.mom.covid.data.ScreeningCovidAnswer
import kotlinx.android.synthetic.main.activity_intro.*
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
    var screeningCovidAnswer: ScreeningCovidAnswer = ScreeningCovidAnswer()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        tv_mulai.setOnClickListener {
            createDialog("Pastikan anda menjawab dengan benar!",{
                ll_awal.visibility = View.GONE
                rl_pertanyaan.visibility = View.VISIBLE
            })
        }
        btn_ya.setOnClickListener {
            if (currentQuetion<5){
                saveAnswer("Ya")
                currentQuetion+=1
                changeQuetion()
            } else{
                saveAnswer("Ya")
                updateData()
            }
        }
        btn_tidak.setOnClickListener {
            if (currentQuetion<5){
                saveAnswer("Ya")
                currentQuetion+=1
                changeQuetion()
            } else{
                saveAnswer("Tidak")
                updateData()
            }
        }

    }

    fun updateData(){
        var user = userPref.getUser()
        user.covid_checked = true
        userPref.setUser(user)
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(userPref.getUser()))
        val call: Call<DataResponse<Any>> = baseApi.updateDataMom("${Urls.registerMother}/${userPref.getUser().id}",requestBody)
        call.enqueue(object : Callback<DataResponse<Any>> {
            override fun onResponse(call: Call<DataResponse<Any>>?, response: Response<DataResponse<Any>>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299){
                        createDialog(response.body()!!.message,{
                            finish()
                        })
                    } else{
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@ScreeningCovidActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                Toast.makeText(this@ScreeningCovidActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun saveAnswer(jawaban: String) {
        if (currentQuetion==1){
            screeningCovidAnswer.pertanyaan_1 = jawaban
        } else if (currentQuetion==2){
            screeningCovidAnswer.pertanyaan_2 = jawaban
        } else if (currentQuetion==3){
            screeningCovidAnswer.pertanyaan_3 = jawaban
        } else if (currentQuetion==4){
            screeningCovidAnswer.pertanyaan_4 = jawaban
        } else if (currentQuetion==5){
            screeningCovidAnswer.pertanyaan_5 = jawaban
        }
    }

    private fun changeQuetion() {
        if (currentQuetion==2){
            tv_pertanyaan.text = resources.getString(R.string.pertanyaan_covid_2)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.pertanyaan_2)).into(iv_pertanyaan)
        } else if (currentQuetion==3){
            tv_pertanyaan.text = resources.getString(R.string.pertanyaan_covid_3)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.pertanyaan_3)).into(iv_pertanyaan)
        } else if (currentQuetion==4){
            tv_pertanyaan.text = resources.getString(R.string.pertanyaan_covid_4)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.pertanyaan_4)).into(iv_pertanyaan)
        } else if (currentQuetion==5){
            tv_pertanyaan.text = resources.getString(R.string.pertanyaan_covid_5)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.pertanyaan_5)).into(iv_pertanyaan)
        }
    }
}