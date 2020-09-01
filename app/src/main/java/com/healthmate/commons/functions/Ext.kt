package com.healthmate.common.functions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.healthmate.R
import com.healthmate.common.adapter.GridSpacingItemDecoration
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.adapter.RecyclerViewTouchListener
import com.healthmate.common.customView.RichTextView
import java.lang.IndexOutOfBoundsException
import java.text.NumberFormat
import java.util.*

fun TextInputEditText.onSubmit(func: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            func()
        }

        true

    }
}

fun Context.closeKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun RecyclerView.setOnClick(clickListener: RecyclerViewClickListener?){
    this.layoutManager = LinearLayoutManager(context!!)
    this.addOnItemTouchListener(RecyclerViewTouchListener(context!!, this, clickListener))
}

fun RecyclerView.setOnClickHorizontal(clickListener: RecyclerViewClickListener?){
    this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
    this.addOnItemTouchListener(RecyclerViewTouchListener(context!!, this, clickListener))
}

fun RecyclerView.setGridOnClick(activity: Activity,clickListener: RecyclerViewClickListener?){
    this.layoutManager = GridLayoutManager(context, 4)
    this.addItemDecoration(GridSpacingItemDecoration(4, activity,0 , true))
    this.addOnItemTouchListener(RecyclerViewTouchListener(context!!, this, clickListener))
}

fun String?.replaceEmpty(text: String = "-"): String {
    try {
        if (this==null) return text
        else if (this.equals("null")) return text
        else if (this.equals("")) return text
        else return this
    } catch (e: IllegalArgumentException){
        return text
    }

}

fun Int?.replaceEmpty(value: Int = 0): Int {
    try {
        if (this==null) return value
        else return this
    } catch (e: IllegalArgumentException){
        return value
    }

}

fun TextView.setTextWithQuery(context: Context, text: String, query: String = "") {
    if (query.equals("") && !text.toLowerCase(Locale.getDefault()).contains(query)) {
        this.text = text.replaceEmpty()
    } else {
        try {
            this.setText(RichTextView(text).setBold(query).setTextColor(query, ContextCompat.getColor(context, R.color.colorAccent)))
        } catch (e: IndexOutOfBoundsException) {
            this.text = text.replaceEmpty()
            e.printStackTrace()
        }catch (e: java.lang.IllegalArgumentException){
            this.text = text.replaceEmpty()
            e.printStackTrace()
        }
    }
}

fun Int.toCurrency(): String {
    val myformat = NumberFormat.getCurrencyInstance(Locale.US)
    var result = myformat.format(this).toString().replace(".00", "").replace(",", ".").replace("$", "")
    result = result.replace(",-", "")
    return result.replace(",00", "")
}

fun String.toCurrency(): String {
    try{
        val myformat = NumberFormat.getCurrencyInstance(Locale.US)
        var result = myformat.format(this).toString().replace(".00", "").replace(",", ".").replace("$", "")
        result = result.replace(",-", "")
        return result.replace(",00", "")
    }catch (e: IllegalArgumentException){
        return this
    }

}

fun Double.toCurrency(): String {
    val myformat = NumberFormat.getCurrencyInstance(Locale.US)
    var result = myformat.format(this).toString().replace(".00", "").replace(",", ".").replace("$", "")
    result = result.replace(",-", "")
    return result.replace(",00", "")
}