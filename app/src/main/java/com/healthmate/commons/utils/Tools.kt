package com.healthmate.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.BatteryManager
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.healthmate.common.sharedpreferences.UserPref
import com.healthmate.menu.auth.view.SigninActivity
import com.healthmate.menu.reusable.data.User
import java.lang.reflect.InvocationTargetException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import com.healthmate.api.Result
import com.healthmate.common.viewmodel.Resource


class Tools {
    fun handleError(activity: Activity, response: Resource<Any>, showDialog: Boolean = true, isNeedClose: Boolean = false){
        print("response: ${Gson().toJson(response)}")
        var message = "Terjadi kesalahan"
        var response_code = "Info"
        if (response.responseCode != 0) response_code = " Info [${response.responseCode}]"
        if (!response.message.equals("")) message = response.message
        if ((showDialog || response.responseCode==401) && !message.toLowerCase(Locale.getDefault()).contains("failed to connect")){
            val dialog = MaterialDialog(activity).title(null,response_code)
                .message(null, message)
                .positiveButton(null,"OK",{
                    if (response.responseCode==401){
                        val dataManager = UserPref(activity)
                        dataManager.setUser(User())
                        activity.startActivity(Intent(activity, SigninActivity::class.java))
                        activity.finish()
                    }else{
                        if (isNeedClose){
                            activity.finish()
                        }
                    }
                    it.dismiss()
                }).noAutoDismiss().cancelable(false)
            dialog.show()
        }
    }

    fun getDiffTimeLong(lastTime: Long): Long{
        val current_time = getCurrentTimeLong()
        return current_time - lastTime
    }


    fun isDateToday(date: String): Boolean{
        println("lol isDateToday date.length: ${date.length}")
        if (date.length>=10){
            val dateOnly = date.substring(0,10)
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val currentDate = sdf.format(Date())
            println("lol isDateToday dateOnly: $dateOnly")
            println("lol isDateToday currentDate: $currentDate")
            if (currentDate.equals(dateOnly)) return true
        }

        return false
    }

    fun getNaturalDuration(second: Double): String{
        println("lol getNaturalDuration second: $second")
        val hour = Math.floor(second / 3600).toInt()
        val minute = Math.floor((second % 3600) / 60).toInt()
        val secondx = Math.floor(second % 60).toInt()
        return "${hour}h ${minute}m ${secondx}s"
    }

    fun buildDateString(day: Int, month: Int, year: Int): String{
        var dayString = day.toString()
        if (day<10) dayString = "0$day"
        var monthString = month.toString()
        if (month<10) monthString = "0$month"
        return "$year-$monthString-$dayString"
    }

    fun buildTimeStampString(day: Int, month: Int, year: Int): String{
        var dayString = day.toString()
        if (day<10) dayString = "0$day"
        var monthString = month.toString()
        if (month<10) monthString = "0$month"
        return "$year-$monthString-$dayString 09:00:00"
    }

    fun buildDateStampString(day: Int, month: Int, year: Int): String{
        val randomHour = (0..24).shuffled().first()
        val randomMinute = (0..60).shuffled().first()
        val randomSecond = (0..60).shuffled().first()
        var dayString = day.toString()
        if (day<10) dayString = "0$day"
        var monthString = month.toString()
        if (month<10) monthString = "0$month"
        return "$year-$monthString-$dayString"
    }

    fun getDateToday(): String{
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val currentDate = sdf.format(Date())
        return currentDate
    }

    fun getMonthToday(): String{
        val sdf = SimpleDateFormat("MM", Locale.US)
        val currentDate = sdf.format(Date())
        return currentDate
    }

    fun getYearMonth(): Int{
        val dateToday = getDateToday()
        val dateTodays = dateToday.split("-")
        var year = "2019"
        var month = "01"
        var yearMonth = "201901"
        if (dateTodays.size>1){
            yearMonth = dateTodays.get(0)+dateTodays.get(1)
        }
        return yearMonth.toInt()
    }

    fun getYearNow(): Int{
        val dateToday = getDateToday()
        val dateTodays = dateToday.split("-")
        var year = "2019"
        if (dateTodays.size>1){
            year = dateTodays.get(0)
        }
        return year.toInt()
    }

    fun getMonthNow(): Int{
        val dateToday = getDateToday()
        val dateTodays = dateToday.split("-")
        var month = "01"
        if (dateTodays.size>1){
            month = dateTodays.get(1)
        }
        return month.toInt()
    }

    fun getNaturalFormatDate(dateString: String, dateFormat: String = YYYY_MM_DD_HH_MM_SS): String{
        try{
            val formatInput = SimpleDateFormat(dateFormat, Locale("id", "US"))
            val dateInput  = formatInput.parse(dateString)
            val formatOutput = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale("id", "US"))
            val dateOutputString = formatOutput.format(dateInput)
            return dateOutputString
        }catch (e: InvocationTargetException){
            e.printStackTrace()
        }catch (e: ParseException){
            e.printStackTrace()
        }catch (e: RuntimeException){
            e.printStackTrace()
        }
        return dateString
    }

    fun getTodayLong(): Long{
        val df = SimpleDateFormat("yyyy-MM-dd",Locale.US)
        val timestamp = df.parse(df.format(Date())).time
        return timestamp
    }

    fun getCurrentTime(): String{
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US)
        val currentDate = sdf.format(Date())
        return currentDate
    }

    fun getCurrentTimeLong(addMilis: Long = 0): Long{
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US)
        try {
            val timestamp = df.parse(getCurrentTime()).time/1000
            println("offlineQR timestamp: $timestamp | addMilis: $addMilis")
            return timestamp + addMilis
        }catch (e: ParseException){
            e.printStackTrace()
        }
        return 0
    }

    fun verifyAvailableNetwork(activity: Activity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

    fun getBatteryPercentage(context: Context): Int {

        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, iFilter)

        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

        val batteryPct = level / scale.toFloat()

        return (batteryPct * 100).toInt()
    }

    fun getAsterik(text: String): String{
        var asterik = ""
        for (i in 0..text.length-3){
            asterik+="*"
        }
        asterik+=text.substring(text.length-3)
        return asterik
    }
}