package com.healthmate.menu.intro.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.navigation.Navigator
import com.healthmate.common.sharedpreferences.AppPref
import com.healthmate.common.sharedpreferences.UserPref
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : BaseActivity() {
    var permissionCount = 0
    var imageNow = 1

    override fun getView(): Int = R.layout.activity_intro

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        perrmission()
        userPref = UserPref(this)
        appPref = AppPref(this)
        appPref.setNeedIntro(false)
        setImage()
        tv_skip.setOnClickListener {
            navigator.signin(this,true)
        }
        tv_next.setOnClickListener {
            if (imageNow<5){
                imageNow+=1
                setImage()
            } else{
                navigator.signin(this, true)
            }
        }
    }

    private fun setImage() {
        if (imageNow==1){
            tv_judul.text = getString(R.string.judul_intro_1)
            tv_keterangan.text = getString(R.string.keterangan_intro_1)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.intro_1)).into(iv_image)
        } else if (imageNow==2){
            tv_judul.text = getString(R.string.judul_intro_2)
            tv_keterangan.text = getString(R.string.keterangan_intro_2)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.intro_2)).into(iv_image)
        } else if (imageNow==3){
            tv_judul.text = getString(R.string.judul_intro_3)
            tv_keterangan.text = getString(R.string.keterangan_intro_3)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.intro_3)).into(iv_image)
        } else if (imageNow==4){
            tv_judul.text = getString(R.string.judul_intro_4)
            tv_keterangan.text = getString(R.string.keterangan_intro_4)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.intro_4)).into(iv_image)
        } else if (imageNow==5){
            tv_next.text = "MASUK"
            tv_judul.text = getString(R.string.judul_intro_5)
            tv_keterangan.text = getString(R.string.keterangan_intro_5)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.intro_5)).into(iv_image)
        }
    }

    private fun perrmission() {
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionCount++
        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionCount++
        } else {
            requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionCount++
        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionCount++
        } else {
            requestPermission(Manifest.permission.CAMERA)
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionCount++
        } else {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }

    private fun requestPermission(permission: String) {
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(permission).subscribe(object : io.reactivex.Observer<Boolean> {
            override fun onNext(t: Boolean) {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        })
    }
}