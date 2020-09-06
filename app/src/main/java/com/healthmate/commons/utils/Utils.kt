package com.healthmate.common.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.google.android.material.textfield.TextInputLayout
import com.healthmate.R
import com.healthmate.common.constant.Var
import com.healthmate.common.customView.RichTextView
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

fun String?.getValue(defaultValue: String = ""): String {
    if (this==null) return defaultValue
    return this
}

fun String.toSentenceCase(): String {
    ifBlank { return "" }
    ifEmpty { return "" }
    if (this.length == 1) return this.toUpperCase()


    val words = this.toLowerCase().split(" ").toMutableList()

    var output = ""

    for (word in words) {
        output += word.capitalize() + " "
    }

    output = output.trim()
    return output
}

fun Long.toCurrency(): String {
    val myformat = NumberFormat.getCurrencyInstance(Locale.US)
    var result = myformat.format(this).toString().replace(".00", "").replace(",", ".").replace("$", "")
    result = result.replace(",-", "")
    return result.replace(",00", "")
}

fun Int.toCurrency(): String {
    val myformat = NumberFormat.getCurrencyInstance(Locale.US)
    var result = myformat.format(this).toString().replace(".00", "").replace(",", ".").replace("$", "")
    result = result.replace(",-", "")
    return result.replace(",00", "")
}

fun String?.toCurrency(): String {
    try{
        val myformat = NumberFormat.getCurrencyInstance(Locale.US)
        var result = myformat.format(this!!.toDouble()).toString().replace(".00", "").replace(",", ".").replace("$", "")
        result = result.replace(",-", "")
        return result.replace(",00", "")
    }catch (e: IllegalArgumentException){
        println("lol toCurrency this: $this , IllegalArgumentException : $e")
        return "-"
    }catch (e: NullPointerException){
        println("lol toCurrency this: $this , NullPointerException : $e")
        return "null"
    }

}

fun Double.toCurrency(): String {
    val myformat = NumberFormat.getCurrencyInstance(Locale.US)
    var result = myformat.format(this).toString().replace(".00", "").replace(",", ".").replace("$", "")
    result = result.replace(",-", "")
    return result.replace(",00", "")
}

fun Double.toCurrencyRupiah(): String {
    return "Rp"+this.toCurrency()
}

fun Int.toCurrencyRupiah(): String {
    return "Rp"+this.toCurrency()
}

fun String.toCurrencyRupiah(): String {
    return "Rp"+this.toCurrency()
}

fun Int.getBooleanValue(trueValue: String, falseValue: String): String {
    if (this==1) return trueValue
    else if (this==0) return falseValue
    return this.toString()
}

fun String.toDateLong(format: String = "yyyy-MM-dd"): Long {
    val df = SimpleDateFormat(format,Locale.US)
    try {
        return df.parse(this).time
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return 0
}

fun String.toTimeStampLong(format: String = "yyyy-MM-dd HH:mm:ss"): Long {
    val df = SimpleDateFormat(format,Locale.US)
    try {
        val timestamp = df.parse(this).time/1000
        return timestamp
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return 0
}

fun String.getTanggal(): String{
    val chars = this.split("-")
    if (chars.size>2){
        return chars[2]
    }
    return ""
}

fun String.getBulan(): String{
    val chars = this.split("-")
    if (chars.size>1){
        return Var.monthFull[chars[1].toInt()-1]
    }
    return ""
}

fun String.getTahun(): String{
    val chars = this.split("-")
    if (chars.size>0){
        return chars[0]
    }
    return ""
}

fun String.getHari(): String{
    val dayDiff = this.dayDiff().toInt()
    if (dayDiff==0){
        return "Hari ini"
    }else if (dayDiff==1){
        return "Kemaren"
    }else if (dayDiff==2){
        return "Kemaren Lusa"
    }
    return this.toViewDate("EEEE")
}

fun String.dayDiff(format: String = "yyyy-MM-dd"): Long {
    val sdf = SimpleDateFormat(format,Locale.US)
    val dateLongA = sdf.parse(this).time
    val dateLongNow = sdf.parse(sdf.format(Date())).time
    val diffLong = dateLongNow - dateLongA
    val diffDay = TimeUnit.DAYS.convert(diffLong, TimeUnit.MILLISECONDS)
    return diffDay
}

fun String.addProsentase(): String {
    return this+"%"
}

fun Double.addProsentase(): String {
    return "$this%"
}

val YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'"
val DD_MMM_YYYY = "dd MMM yyyy"
val DD_MMM_YYYY_HH_mm_ss = "dd MMM yyyy, HH:mm"
val YYYY_MM_DD = "yyyy-MM-dd"
val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm"
fun String.toViewDate(toFormatCustom: String = DD_MMM_YYYY_HH_mm_ss,fromFormatCustom: String = YYYY_MM_DD_HH_MM_SS): String {
    try {
        var fromFormat = SimpleDateFormat(fromFormatCustom, Locale("en", "US"))
        var toFormat = SimpleDateFormat(toFormatCustom, Locale("id", "ID"))
        if (this.length ==10){
            fromFormat = SimpleDateFormat(YYYY_MM_DD, Locale("en", "US"))
            toFormat = SimpleDateFormat(toFormatCustom, Locale("id", "ID"))
        }
        fromFormat.setLenient(false)
        toFormat.setLenient(false)
        val date = fromFormat.parse(this)
        return toFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
        return this
    }
}

fun String.toViewDateOnly(toFormatCustom: String = DD_MMM_YYYY,fromFormatCustom: String = YYYY_MM_DD): String {
    try {
        var fromFormat = SimpleDateFormat(fromFormatCustom, Locale("en", "US"))
        var toFormat = SimpleDateFormat(toFormatCustom, Locale("id", "ID"))
        if (this.length ==10){
            fromFormat = SimpleDateFormat(YYYY_MM_DD, Locale("en", "US"))
            toFormat = SimpleDateFormat(toFormatCustom, Locale("id", "ID"))
        }
        fromFormat.setLenient(false)
        toFormat.setLenient(false)
        val date = fromFormat.parse(this)
        return toFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
        return this
    }
}




fun View.hideIfEmpty(value: String){
    if (value.equals("")) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun TextView.hideIfEmpty(value: String){
    if (value.equals("")){
        this.visibility = View.GONE
        this.text = value
    }else this.visibility = View.VISIBLE
}

fun String?.replaceEmpty(text: String = "-"): String {
    if (this==null) return text
    else if (this.equals("null")) return text
    else if (this.equals("")) return text
    else return this
}

fun Int.getTwoChars(): String{
    if (this<10 && this>=0) return "0$this"
    return this.toString()
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun String.getSplits(position: Int = 1,separator: String = " "): String {
    val strings = this.split(separator)
    try{
        return strings[position]
    }catch (e: IndexOutOfBoundsException){
        return ""
    }
}

fun String?.defaultValue(value: String = ""): String {
    try{
        if (this==null) return value
        else return this
    }catch (e: NullPointerException){
        return value
    }
}

fun String.explodeToArray(separator: String = ","): ArrayList<String> {
    return ArrayList(this.split(separator))
}

fun ArrayList<String>.toString(separator: String = ","): String {
    var temp = ""
    for (x in 0..this.size-1){
        if (x<this.size-1) temp += this[x]+","
        else temp += this[x]
    }
    return temp
}

fun String.getInitial(count: Int = 1,separator: String = " "): String {
    val words = this.split(separator)
    var inisial = ""
    var total = 1
    for (word in words){
        total++
        val chars = word.toCharArray()
        if (chars.size>0){
            inisial = inisial+chars.get(0).toString()
        }
        if (total>count) return inisial.toUpperCase()
    }

    return this
}

fun TextInputLayout.markRequiredInRed() {
    hint = buildSpannedString {
        append(hint)
        color(Color.RED) { append(" *") } // Mind the space prefix.
    }
}

fun String.withQuery(mContext: Context,query: String): CharSequence{
    try {
        return RichTextView(this)
            .setBold(query.toLowerCase(Locale.getDefault()))
            .setTextColor(query, ContextCompat.getColor(mContext, R.color.colorAccent))
    } catch (e: IndexOutOfBoundsException) {
        return this
    }
}

fun AppCompatTextView.strikeThru() {
    this.setPaintFlags(this.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
}


